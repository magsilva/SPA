package redestcc;

import java.awt.Color;
import java.io.File;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder.DegreeRangeFilter;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2Builder;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;

public class GraphToolkitExample
{
	public GraphToolkitExample(File filename)
	{
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		 
		//Get models and controllers for this new workspace - will be useful later
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
		PreviewModel model = Lookup.getDefault().lookup(PreviewController.class).getModel();
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
		RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);
		 
		//Import file       
		Container container;
		try {
		    // File file = new File(getClass().getResource("/org/gephi/toolkit/demos/resources/polblogs.gml").toURI());
		    container = importController.importFile(filename);
		    container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED);
		} catch (Exception e) {
		    throw new IllegalArgumentException(e);
		}
		 
		//Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);
		 
		//See if graph is well imported
		DirectedGraph graph = graphModel.getDirectedGraph();
		System.out.println("Nodes: " + graph.getNodeCount());
		System.out.println("Edges: " + graph.getEdgeCount());
		 
		//Filter      
		DegreeRangeFilter degreeFilter = new DegreeRangeFilter();
		degreeFilter.init(graph);
		degreeFilter.setRange(new Range(1, Integer.MAX_VALUE));     //Remove nodes with degree < 1
		Query query = filterController.createQuery(degreeFilter);
		GraphView view = filterController.filter(query);
		graphModel.setVisibleView(view);
		 
		//Run YifanHuLayout for 100 passes - The layout always takes the current visible view
		ForceAtlas2Builder layoutBuilder = new ForceAtlas2Builder();
		ForceAtlas2 layout = layoutBuilder.buildLayout();
		layout.setGraphModel(graphModel);
		layout.resetPropertiesValues();
		layout.setBarnesHutOptimize(true);
		layout.setGravity(2.0);
		layout.setEdgeWeightInfluence(0.0);
		layout.setThreadsCount(2);
		layout.setOutboundAttractionDistribution(true);
		layout.setScalingRatio(3.0);
		layout.setJitterTolerance(0.05);
		layout.initAlgo();
		 
		for (int i = 0; i < 200 && layout.canAlgo(); i++) {
		    layout.goAlgo();
		}
		layout.endAlgo();
		 
		//Get Centrality
		GraphDistance distance = new GraphDistance();
		distance.setDirected(true);
		distance.execute(graphModel, attributeModel);
		 
		//Rank color by Degree
		Ranking degreeRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING);
		AbstractColorTransformer colorTransformer = (AbstractColorTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_COLOR);
		colorTransformer.setColors(new Color[]{new Color(0xFEF0D9), new Color(0xB30000)});
		rankingController.transform(degreeRanking,colorTransformer);
		 
		//Rank size by centrality
		AttributeColumn centralityColumn = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
		Ranking centralityRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
		AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_SIZE);
		sizeTransformer.setMinSize(3);
		sizeTransformer.setMaxSize(10);
		rankingController.transform(centralityRanking,sizeTransformer);
		 
		//Preview
		model.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
		model.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.FALSE);
		model.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(Color.GRAY));
		model.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, new Float(0.1f));
		model.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, model.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont(8));
		 
		//Export
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		try {
		    ec.exportFile(new File("/home/magsilva/headless_simple.pdf"));
		} catch (Exception e) {
		    throw new IllegalArgumentException(e);
		}
	}
	
	public static void main(String[] args)
	{
		GraphToolkitExample gte = new GraphToolkitExample(new File("/home/magsilva/redeOldsNewsCompletav2.gml"));
	}
}
