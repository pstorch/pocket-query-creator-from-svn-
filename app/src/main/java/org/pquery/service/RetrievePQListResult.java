package org.pquery.service;

import org.pquery.dao.PQ;
import org.pquery.webdriver.FailurePermanentException;

public class RetrievePQListResult {
    public FailurePermanentException failure;
    public PQ[] pqs = null;
    public PQ[] repeatables = null;

    public RetrievePQListResult(FailurePermanentException failure) {
        this.failure = failure;
    }

    public RetrievePQListResult(PQ[] pqs, PQ[] repeatables) {
        this.pqs = pqs;
        this.repeatables = repeatables;
    }

    public RetrievePQListResult() {
    }
}
