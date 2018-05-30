package com.trafficticketbuddy.client.interfaces;

import com.trafficticketbuddy.client.model.cases.Response;

import java.util.List;

/**
 * Created by User on 26-05-2018.
 */

public interface MyCaseOpenCaseDataLoaded {
    public void openCaseDataLoaded(List<Response> caseListData);
}
