package elements.templates;


import elements.VariableInfo;

import java.util.List;

class Template {

    private final long id;
    private final List<VariableInfo> variablesInfo;

    Template(long id, List<VariableInfo> variablesInfo) {
        this.id = id;
        this.variablesInfo = variablesInfo;
    }

    public long getId() {
        return id;
    }

    public List<VariableInfo> getVariablesInfo() {
        return variablesInfo;
    }
}
