package com.eames.masterkey.service.progression.services.totalposition;

import com.eames.masterkey.model.BittingGroup;
import com.eames.masterkey.model.BittingList;
import com.eames.masterkey.model.BittingNode;
import com.eames.masterkey.model.KeyBitting;
import com.eames.masterkey.service.progression.ProgressionServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TotalPositionProgressionServiceTest {

    /**
     * Gets called before each test.
     */
    @Before
    public void setUp() {

    }

    /**
     * Gets called after each test.
     */
    @After
    public void tearDown() {

    }

    /**
     * .generateBittingList() tests
     */

    @Test
    public void testGenerateBittingList_Null() {

        try {

            TotalPositionProgressionService service = new TotalPositionProgressionService(null);

            // Throws: ProgressionServiceException
            service.generateBittingList();

            fail();

        } catch (ProgressionServiceException e) {

            // Expected result...
        }
    }

    @Test
    public void testGenerateBittingList_6x10x2() {

        try {

            // Throws: ValidationException
            TotalPositionProgressionCriteria criteria = new TotalPositionProgressionCriteria.Builder()
                    .setMACS(4)
                    .setMasterCuts(new int[]{2, 5, 7, 4, 5, 9})
                    .setProgressionSteps(new int[][]{
                            {4, 1, 9, 0, 7, 7},
                            {0, 3, 5, 8, 3, 5},
                            {6, 9, 3, 6, 1, 3},
                            {8, 7, 1, 2, 9, 1}
                    })
                    .setProgressionSequence(new int[]{5, 3, 1, 6, 4, 2})
                    .build();

            TotalPositionProgressionService service = new TotalPositionProgressionService(criteria);
            // Throws: ProgressionServiceException
            BittingList bittingList = service.generateBittingList();

            assertNotNull(bittingList);

            // Test the root bitting group.
            BittingGroup rootBittingGroup = bittingList.getRootBittingGroup();
            assertNotNull(rootBittingGroup);
            KeyBitting master = rootBittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{2, 5, 7, 4, 5, 9}, master.getKey());
            assertFalse(master.getHasMACSViolation());

            // Test the first level groups.
            BittingNode[] bittingNodes = rootBittingGroup.getGroups();
            assertNotNull(bittingNodes);
            // Group 0
            BittingNode bittingNode = bittingNodes[0];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            BittingGroup bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{2, 5, 7, 0, 5, 9}, master.getKey());
            assertTrue(master.getHasMACSViolation());
            // Group 3
            bittingNode = bittingNodes[3];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{2, 5, 7, 2, 5, 9}, master.getKey());
            assertTrue(master.getHasMACSViolation());

            // Test the second level groups.
            bittingNodes = bittingGroup.getGroups();
            assertNotNull(bittingNodes);
            // Group 0
            bittingNode = bittingNodes[0];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{4, 5, 7, 2, 5, 9}, master.getKey());
            assertTrue(master.getHasMACSViolation());
            // Group 3
            bittingNode = bittingNodes[3];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{8, 5, 7, 2, 5, 9}, master.getKey());
            assertTrue(master.getHasMACSViolation());

            // Test the third level groups.
            bittingNodes = bittingGroup.getGroups();
            assertNotNull(bittingNodes);
            // Group 0
            bittingNode = bittingNodes[0];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{8, 5, 7, 2, 7, 9}, master.getKey());
            assertTrue(master.getHasMACSViolation());
            // Group 3
            bittingNode = bittingNodes[3];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{8, 5, 7, 2, 9, 9}, master.getKey());
            assertTrue(master.getHasMACSViolation());

            // Test the fourth level groups.
            bittingNodes = bittingGroup.getGroups();
            assertNotNull(bittingNodes);
            // Group 0
            bittingNode = bittingNodes[0];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{8, 1, 7, 2, 9, 9}, master.getKey());
            assertTrue(master.getHasMACSViolation());
            // Group 3
            bittingNode = bittingNodes[3];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{8, 7, 7, 2, 9, 9}, master.getKey());
            assertTrue(master.getHasMACSViolation());

            // Test the fifth level groups.
            bittingNodes = bittingGroup.getGroups();
            assertNotNull(bittingNodes);
            // Group 0
            bittingNode = bittingNodes[0];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{8, 7, 7, 2, 9, 7}, master.getKey());
            assertTrue(master.getHasMACSViolation());
            // Group 3
            bittingNode = bittingNodes[3];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{8, 7, 7, 2, 9, 1}, master.getKey());
            assertTrue(master.getHasMACSViolation());

            // Test the change keys.
            bittingNodes = bittingGroup.getGroups();
            assertNotNull(bittingNodes);
            // Key 0
            bittingNode = bittingNodes[0];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof KeyBitting);
            KeyBitting keyBitting = (KeyBitting) bittingNode;
            assertArrayEquals(new int[]{8, 7, 9, 2, 9, 1}, keyBitting.getKey());
            assertTrue(keyBitting.getHasMACSViolation());
            // Key 3
            bittingNode = bittingNodes[3];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof KeyBitting);
            keyBitting = (KeyBitting) bittingNode;
            assertArrayEquals(new int[]{8, 7, 1, 2, 9, 1}, keyBitting.getKey());
            assertTrue(master.getHasMACSViolation());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGenerateBittingList_5x6x1() {

        try {

            // Throws: ValidationException
            TotalPositionProgressionCriteria criteria = new TotalPositionProgressionCriteria.Builder()
                    .setMACS(4)
                    .setMasterCuts(new int[]{6, 3, 4, 5, 2})
                    .setProgressionSteps(new int[][]{
                            {4, 1, 6, 3, 5},
                            {1, 2, 5, 6, 3},
                            {3, 4, 3, 4, 1},
                            {2, 5, 2, 1, 6},
                            {5, 6, 1, 2, 4}
                    })
                    .setProgressionSequence(new int[]{1, 2, 3, 4, 5})
                    .build();

            TotalPositionProgressionService service = new TotalPositionProgressionService(criteria);
            // Throws: ProgressionServiceException
            BittingList bittingList = service.generateBittingList();

            assertNotNull(bittingList);

            // Test the root bitting group.
            BittingGroup rootBittingGroup = bittingList.getRootBittingGroup();
            assertNotNull(rootBittingGroup);
            KeyBitting master = rootBittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{6, 3, 4, 5, 2}, master.getKey());
            assertFalse(master.getHasMACSViolation());

            // Test the first level groups.
            BittingNode[] bittingNodes = rootBittingGroup.getGroups();
            assertNotNull(bittingNodes);
            // Group 0
            BittingNode bittingNode = bittingNodes[0];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            BittingGroup bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{6, 3, 4, 5, 5}, master.getKey());
            assertFalse(master.getHasMACSViolation());
            // Group 4
            bittingNode = bittingNodes[4];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{6, 3, 4, 5, 4}, master.getKey());
            assertFalse(master.getHasMACSViolation());

            // Test the second level groups.
            bittingNodes = bittingGroup.getGroups();
            assertNotNull(bittingNodes);
            // Group 0
            bittingNode = bittingNodes[0];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{6, 3, 4, 3, 4}, master.getKey());
            assertFalse(master.getHasMACSViolation());
            // Group 4
            bittingNode = bittingNodes[4];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{6, 3, 4, 2, 4}, master.getKey());
            assertFalse(master.getHasMACSViolation());

            // Test the third level groups.
            bittingNodes = bittingGroup.getGroups();
            assertNotNull(bittingNodes);
            // Group 0
            bittingNode = bittingNodes[0];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{6, 3, 6, 2, 4}, master.getKey());
            assertFalse(master.getHasMACSViolation());
            // Group 4
            bittingNode = bittingNodes[4];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{6, 3, 1, 2, 4}, master.getKey());
            assertFalse(master.getHasMACSViolation());

            // Test the fourth level groups.
            bittingNodes = bittingGroup.getGroups();
            assertNotNull(bittingNodes);
            // Group 0
            bittingNode = bittingNodes[0];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{6, 1, 1, 2, 4}, master.getKey());
            assertTrue(master.getHasMACSViolation());
            // Group 4
            bittingNode = bittingNodes[4];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof BittingGroup);
            bittingGroup = (BittingGroup) bittingNode;
            master = bittingGroup.getMaster();
            assertNotNull(master);
            assertArrayEquals(new int[]{6, 6, 1, 2, 4}, master.getKey());
            assertTrue(master.getHasMACSViolation());

            // Test the change keys.
            bittingNodes = bittingGroup.getGroups();
            assertNotNull(bittingNodes);
            // Key 0
            bittingNode = bittingNodes[0];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof KeyBitting);
            KeyBitting keyBitting = (KeyBitting) bittingNode;
            assertArrayEquals(new int[]{4, 6, 1, 2, 4}, keyBitting.getKey());
            assertTrue(keyBitting.getHasMACSViolation());
            // Key 4
            bittingNode = bittingNodes[4];
            assertNotNull(bittingNode);
            assertTrue(bittingNode instanceof KeyBitting);
            keyBitting = (KeyBitting) bittingNode;
            assertArrayEquals(new int[]{5, 6, 1, 2, 4}, keyBitting.getKey());
            assertTrue(master.getHasMACSViolation());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
