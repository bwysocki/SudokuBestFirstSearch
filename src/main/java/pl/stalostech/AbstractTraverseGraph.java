package pl.stalostech;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public abstract class AbstractTraverseGraph {

    protected String GET_NODE_BY_INDEX = "MATCH (n:Node) WHERE n.name={name} RETURN n AS nodeByIndex";

    /**
     * Returns string representing the nodes visited by algorithm.
     * @param db - graph database to traverse. Algorithm assumes that it will be constructed
     * with nodes labeled as Node having property 'name'.
     * @param startNodeId - the value of 'name' property
     * @return BFS result as String
     */
    public String getInspectedNodesAsString(GraphDatabaseService db, String startNodeId) {
        Queue<Node> nodes = getInspectedNodesAsQueue(db, startNodeId);

        StringBuilder response = new StringBuilder("");

        try (Transaction tx = db.beginTx()) {
            while (!nodes.isEmpty()) {
                Node node = nodes.remove();
                response.append(node.getProperty("name"));
                if (!nodes.isEmpty()) {
                    response.append("-");
                }
            }
            tx.success();
        }

        return response.toString();
    }

    /**
     * Returns nodes in visit order.
     * @param db - graph database to traverse. Algorithm assumes that it will be constructed
     * with nodes labeled as Node having property 'name'.
     * @param startNodeId - the value of 'name' property
     * @return visited nodes
     */
    public abstract Queue<Node> getInspectedNodesAsQueue(GraphDatabaseService db, String startNodeId);

    /**
     * Returns start node as Node object
     * @param db - graph database to traverse. Algorithm assumes that it will be constructed
     * with nodes labeled as Node having property 'name'.
     * @param startNodeId - the value of 'name' property
     * @return Node object
     */
    protected Node getStartNode(GraphDatabaseService db, String startNodeId) {
        ExecutionEngine executionEngine = new ExecutionEngine(db);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", startNodeId);
        ExecutionResult result = executionEngine.execute(GET_NODE_BY_INDEX, params);

        //add first node to the queue
        return (Node) result.iterator().next().get("nodeByIndex");
    }

}
