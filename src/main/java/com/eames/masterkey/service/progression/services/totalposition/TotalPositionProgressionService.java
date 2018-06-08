package com.eames.masterkey.service.progression.services.totalposition;

import com.eames.masterkey.model.BittingList;
import com.eames.masterkey.model.KeyBitting;
import com.eames.masterkey.service.progression.ProgressionServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is responsible for generating a bitting list using the Total Position Progression
 * technique with given progression criteria.
 */
public class TotalPositionProgressionService {

    // Initialize the Log4j logger.
    private static final Logger logger = LogManager.getLogger(TotalPositionProgressionService.class);

    /**
     * Generates a bitting list using the Total Position Progression technique.
     *
     * TODO: Need .generateBittingList() tests.
     *
     * @param criteria the progression criteria
     * @return the newly generated {@link BittingList}
     * @throws ProgressionServiceException if any error occurs
     */
    public BittingList generateBittingList(TotalPositionProgressionCriteria criteria)
        throws ProgressionServiceException {

        if (criteria == null) {

            final String errorMessage = "No progression criteria provided.";
            logger.error(errorMessage);
            throw new ProgressionServiceException(errorMessage);
        }

        /*
         * No need to validate the criteria because they use a builder that guarantees
         * any criteria generated are valid.
         */

        // TODO: Need to actually generate the bitting list.

        BittingList bittingList = new BittingList();
        bittingList.setMaster(new int[] {3, 5, 4, 2, 1, 5});

        KeyBitting[] keyBittings = new KeyBitting[4];
        keyBittings[0] = new KeyBitting(new int[] {1, 5, 4, 2, 1, 5});
        keyBittings[1] = new KeyBitting(new int[] {5, 5, 4, 2, 1, 5});
        keyBittings[2] = new KeyBitting(new int[] {7, 5, 4, 2, 1, 5});
        keyBittings[3] = new KeyBitting(new int[] {9, 5, 4, 2, 1, 5});
        bittingList.setGroups(keyBittings);

        // Return the bitting list.
        return bittingList;
    }
}
