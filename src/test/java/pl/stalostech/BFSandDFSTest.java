package pl.stalostech;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Presenting Breadth First Search - graph traversing algorithm.
 * Graph to traverse:
 *
 * (B)----(F)----(C)
 *  |\     |       \
 *  | \   (D)      (H)
 *  |  \  /
 *  |  (A)
 *  |    \
 * (E)---(G)
 *
 * The purpose of the test is to visit all nodes starting from A.
 * Expected traversing path : A-B-D-G-E-F-C-H
 * @author Bartosz Wysocki
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("/pl/stalostech/test-context.xml"))
public class BFSandDFSTest {

    @Autowired
    private BreadthFirstSearch bfs;

    @Autowired
    private DepthFirstSearch dfs;

    private GraphDatabaseService db;

    private final String graph = "CREATE\n" +
        "(a:Node {name : 'A'}),\n" +
        "(b:Node {name : 'B'}),\n" +
        "(c:Node {name : 'C'}),\n" +
        "(d:Node {name : 'D'}),\n" +
        "(e:Node {name : 'E'}),\n" +
        "(f:Node {name : 'F'}),\n" +
        "(g:Node {name : 'G'}),\n" +
        "(h:Node {name : 'H'}),\n" +
        "b-[:PATH]->a-[:PATH]->b,\n" +
        "d-[:PATH]->a-[:PATH]->d,\n" +
        "g-[:PATH]->a-[:PATH]->g,\n" +
        "e-[:PATH]->b-[:PATH]->e,\n" +
        "f-[:PATH]->b-[:PATH]->f,\n" +
        "h-[:PATH]->c-[:PATH]->h,\n" +
        "f-[:PATH]->c-[:PATH]->f,\n" +
        "f-[:PATH]->d-[:PATH]->f,\n" +
        "g-[:PATH]->e-[:PATH]->g,\n" +
        "c-[:PATH]->h-[:PATH]->c\n";

    private final String graphIndex = "CREATE INDEX ON :Node(name)";

    @Before
    public void setUp() {
        db = new TestGraphDatabaseFactory().newImpermanentDatabase();

        ExecutionEngine engine = new ExecutionEngine(db);
        engine.execute(graph);
        engine.execute(graphIndex);
    }

    @Test
    public void testBFS() {
        assertEquals("A-B-D-G-E-F-C-H", bfs.getInspectedNodesAsString(db, "A"));
        assertEquals("F-B-C-D-A-E-H-G", bfs.getInspectedNodesAsString(db, "F"));
        assertEquals("G-A-E-B-D-F-C-H", bfs.getInspectedNodesAsString(db, "G"));
    }

    @Test
    public void testDFS() {
        assertEquals("A-B-E-G-F-C-H-D", dfs.getInspectedNodesAsString(db, "A"));
        assertEquals("F-B-A-D-G-E-C-H", dfs.getInspectedNodesAsString(db, "F"));
        assertEquals("G-A-B-E-F-C-H-D", dfs.getInspectedNodesAsString(db, "G"));
    }

}
