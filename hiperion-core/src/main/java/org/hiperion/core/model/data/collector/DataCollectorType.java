package org.hiperion.core.model.data.collector;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 15.04.13.
 * Time: 17:29
 */
public enum DataCollectorType {
    PUSH("push"), PULL("pull");

    private final String value;

    private DataCollectorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DataCollectorType getDataCollectorType(String value) {
        for (DataCollectorType dataCollectorType : DataCollectorType.values()) {
            if (dataCollectorType.getValue().equals(value)) {
                return dataCollectorType;
            }
        }

        throw new IllegalArgumentException();
    }
}
