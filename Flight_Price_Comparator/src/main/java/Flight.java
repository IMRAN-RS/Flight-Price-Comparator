public class Flight {
    private String operator;          // Flight Operator
    private String flightNumber;      // Flight Number
    private String priceOnCleartrip;  // Price on Cleartrip
    private String priceOnPaytm;      // Price on Paytm

    // Constructor
    public Flight(String operator, String flightNumber, String priceOnCleartrip, String priceOnPaytm) {
        this.operator = operator;
        this.flightNumber = flightNumber;
        this.priceOnCleartrip = priceOnCleartrip;
        this.priceOnPaytm = priceOnPaytm;
    }

    // Getters
    public String getOperator() {
        return operator;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getPriceOnCleartrip() {
        return priceOnCleartrip;
    }

    public String getPriceOnPaytm() {
        return priceOnPaytm;
    }
}
