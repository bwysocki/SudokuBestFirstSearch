package pl.stalostech;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.Iterables;
import org.springframework.stereotype.Component;

/**
 * The class represents DFS algorithm.
 * As a data-structure it uses queue.
 * @author Bartosz Wysocki
 */
@Component
public class DepthFirstSearch extends AbstractTraverseGraph {

    @Override
    public Queue<Node> getInspectedNodesAsQueue(GraphDatabaseService db, String startNodeId) {

        Queue<Node> response = new LinkedList<Node>();

        Node start = getStartNode(db, startNodeId);

        Stack<Node> childernOfNodes = new Stack<Node>();
        childernOfNodes.push(start);

        try (Transaction tx = db.beginTx()) {
            while (!childernOfNodes.isEmpty()) {
                Node node = childernOfNodes.pop();
                if (!response.contains(node)){
                    response.add(node);
                }
                Iterable<Relationship> relationships = node.getRelationships(Direction.OUTGOING);
                for (Relationship relationship : Iterables.reverse(relationships)) {
                    Node next = relationship.getEndNode();
                    if (!response.contains(next)) {
                        childernOfNodes.push(next);
                    }
                }
            }
            tx.success();
        }

        return response;
    }

}
