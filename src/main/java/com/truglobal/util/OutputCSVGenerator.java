package com.truglobal.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;

public class OutputCSVGenerator {

	private List<String> headers = new ArrayList<>();

	private List<List<String>> allCSVRows = new ArrayList<>();

	/**
	 * @return the headers
	 */
	public List<String> getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public void renameColumn(String columnName, int index) {
		this.headers.set(index, columnName);
	}

	public String getColumnName(int index) {
		return this.headers.get(index);
	}

	public List<String> getRow(int index) {
		return this.allCSVRows.get(index);
	}

	public void setRow(List<String> row, int index) {
		this.allCSVRows.set(index, row);
	}

	public void addRow(List<String> row) {
		this.allCSVRows.add(row);
	}

	public void deleteColumn(int index) {
		try {
			this.headers.remove(index);
		} catch (Exception e) {
		}
		this.allCSVRows.forEach(eachrow -> {
			try {
				eachrow.remove(index);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @return the allCSVRows
	 */
	public List<List<String>> getAllCSVRows() {
		return allCSVRows;
	}

	public void generateCSV(String fileName) throws IOException {
		fileName = fileName.endsWith(".csv") ? fileName : fileName + ".csv";
		try (CSVWriter writer = new CSVWriter(new FileWriter(new File(fileName)))) {
			writer.writeNext(headers.toArray(String[]::new));
			allCSVRows.stream().forEach(eachrow -> {
				writer.writeNext(eachrow.toArray(String[]::new));
			});
		}
	}

//	public static void main(String[] args) throws IOException {
//		OutputCSVGenerator output = new OutputCSVGenerator();
//		List<String> headers = new ArrayList<>();
//		headers.add("col1");
//		headers.add("col2");
//		headers.add("col3");
//
//		output.setHeaders(headers);
//
//		List<String> row = new ArrayList<>();
//		row.add("Hi");
//		row.add("Kumar");
//		row.add("Swamy");
//		output.addRow(row);
//
//		output.renameColumn("col4", 1);
//		
//		output.deleteColumn(0);
//
//		output.generateCSV("C:\\Users\\admin\\Downloads\\k.csv");
//		System.out.println("Done");
//	}

}
