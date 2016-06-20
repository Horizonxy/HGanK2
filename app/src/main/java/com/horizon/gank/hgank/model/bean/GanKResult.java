package com.horizon.gank.hgank.model.bean;


import java.io.Serializable;
import java.util.List;

public class GanKResult<T> implements Serializable {

    private boolean error;
    private List<T> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
