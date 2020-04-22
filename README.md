# java_timeseries_arima
Java application that supports TimeSeries ARIMA modeling, and forecasting.

## Supported input files

This application supports the loading of two file types.

### UCR file

A file that contains a TimeSeries as a row, in which the first value is a classification label.
(These files were found in UCR Achive 2018)

```
You should specify the row that contains the TimeSeries data.
You should specify the separator of the selected row.
The first column of selected row will not be used. (Label column in UCR files)
```

### Column file

A file that contains a TimeSeries as a column.

```
You should specify the separator of this row.
You should specify if the first line is the header line.
You should specify the column that contains the TimeSeries.
```
