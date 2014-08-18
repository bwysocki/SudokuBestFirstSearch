package pl.stalostech;

import java.util.LinkedList;
import java.util.Queue;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.springframework.stereotype.Component;

/**
 * The class represents BFS algorithm.
 * As a data-structure it uses queue.
 * @author Bartosz Wysocki
 */
@Component
public class BreadthFirstSearch extends AbstractTraverseGraph {

    @Override
    public Queue<Node> getInspectedNodesAsQueue(GraphDatabaseService db, String startNodeId) {

        Queue<Node> response = new LinkedList<Node>();

        Node start = getStartNode(db, startNodeId);
        response.add(start);

        Queue<Node> childernOfNodes = new LinkedList<Node>();
        childernOfNodes.add(start);

        try (Transaction tx = db.beginTx()) {
            while (!childernOfNodes.isEmpty()) {
                Node node = childernOfNodes.remove();
                Iterable<Relationship> relationships = node.getRelationships(Direction.OUTGOING);
                for (Relationship relationship : relationships) {
                    Node next = relationship.getEndNode();
                    if (!response.contains(next)) {
                        childernOfNodes.add(next);
                        response.add(next);
                    }
                }
            }
            tx.success();
        }

        return response;
    }

}
