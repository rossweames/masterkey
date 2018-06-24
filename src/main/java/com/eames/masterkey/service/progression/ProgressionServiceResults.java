package com.eames.masterkey.service.progression;

import com.eames.masterkey.model.BittingList;

/**
 * This class holds the results of Progression Services' bitting list generations.
 */
public class ProgressionServiceResults {

    // The name of the service that generated this bitting list.
    private String source;

    // The criteria that were used to generate tge bitting group.
    private ProgressionCriteria criteria;

    // The generated bitting list.
    private BittingList bittingList;

    /**
     * Constructor
     *
     * @param source the source progression service
     * @param criteria the criteria used to generate the bitting list
     * @param bittingList the generated bitting list
     */
    public ProgressionServiceResults(String source, ProgressionCriteria criteria, BittingList bittingList) {

        this.source = source;
        this.criteria = criteria;
        this.bittingList = bittingList;
    }

    /**
     * Gets the name of the service that generated these results.
     *
     * @return the name of the service
     */
    public String getSource() {
        return source;
    }

    /**
     * Gets the {@link ProgressionCriteria} used to generate the bitting list.
     *
     * @return the criteria
     */
    public ProgressionCriteria getCriteria() {
        return criteria;
    }

    /**
     * Gets the generated {@link BittingList}.
     *
     * @return the bitting list
     */
    public BittingList getBittingList() {
        return bittingList;
    }
}
