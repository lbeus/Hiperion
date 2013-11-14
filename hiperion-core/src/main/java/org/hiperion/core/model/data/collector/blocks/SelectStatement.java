package org.hiperion.core.model.data.collector.blocks;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 13.04.13.
 * Time: 22:21
 */
public class SelectStatement {
    private String dataSourceName;
    private String dataFieldName;
    private String outputDataFieldName;

    public SelectStatement(String dataSourceName,
                           String dataFieldName, String outputDataFieldName) {
        this.dataSourceName = dataSourceName;
        this.dataFieldName = dataFieldName;
        this.outputDataFieldName = outputDataFieldName;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public String getDataFieldName() {
        return dataFieldName;
    }

    public String getOutputDataFieldName() {
        return outputDataFieldName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SelectStatement that = (SelectStatement) o;

        if (dataFieldName != null ? !dataFieldName.equals(that.dataFieldName) : that.dataFieldName != null)
            return false;
        if (dataSourceName != null ? !dataSourceName.equals(that.dataSourceName) : that.dataSourceName != null)
            return false;
        if (outputDataFieldName != null ? !outputDataFieldName.equals(that.outputDataFieldName) : that.outputDataFieldName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 31 * (dataSourceName != null ? dataSourceName.hashCode() : 0);
        result = 31 * result + (dataFieldName != null ? dataFieldName.hashCode() : 0);
        result = 31 * result + (outputDataFieldName != null ? outputDataFieldName.hashCode() : 0);
        return result;
    }
}
