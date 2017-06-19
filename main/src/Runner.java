import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Connor on 6/16/2017.
 **/
public class Runner {
	public static void main(String args[]) {
		JFrame window = new JFrame("Craigslist Crawler");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Scanner x = new Scanner(System.in);

		System.out.print("Input region:\n" +
			             "(0) Jersey Shore\n" +
			             "(1) Raleigh\n");
		int region = x.nextInt();

		System.out.print("Input category:\n" +
			             "(0) Computer Systems\n" +
			             "(1) Auto Parts\n" +
			             "(2) Cars by owner\n" +
			             "(3) All\n");
		int category = x.nextInt();

        Query query = new Query(region, category, false);

		ArrayList<SearchResult> results = query.getResults();

		for (SearchResult s : results)
			System.out.println(String.format("$%-4d - %-90s / %s", s.getPrice(), s.getTitle(), s.getLink()));

		System.out.println();
		System.out.println("Success! Found " + results.size() + " Results.");

		x.close();
	}
}

