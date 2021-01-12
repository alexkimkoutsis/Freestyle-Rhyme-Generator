package rhymePackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/* Copyright (C) 2020 Alex Kim-Koutsis */


/* This program is designed to function as a rhyme generator.
 * It was created to aid in freestyle rapping practice, so that users
 * could focus on perfecting their flow and sentence/context generation abilities
 * without having to worry about coming up with rhymes at the same time.
 * It can also be used by more experience freestyle rappers to simply expand
 * their rhyming vocabulary and encourage use of words that they normally wouldn't use.
 * 
 * This program begins by asking how many rhymes the user would like to receive
 * per input word. 
 * 
 * Then, the user may input any standard English word and the program will return
 * the words that rhyme with it. If in the first step, the user specified that they wanted
 * x rhymes per input word, then x rhymes would be printed.
 * 
 * The program will then prompt the user for an input word again.
 * 
 * The program will keep prompting the user for input words until they input "!",
 * at which point the program will terminate.
 * 
 * Note that if the user inputs any non-standard English words, no rhymes will be generated,
 * and there can be no spaces in the input word.
 * 
 * Credit goes to the Datamuse API for rhyme generation. */

public class Main {

	private static HttpURLConnection connection;
	
	public static void main(String[] args) {
		
		BufferedReader reader;
		String line;
		StringBuffer responseContent = new StringBuffer();
		Scanner scanner = new Scanner(System.in);
		String word;
		String urlString;
		int numRhymes = 0;
		
		// Program begins and prompts user for the number of words they'd like to receive.
		System.out.println("Welcome. This program will find rhymes for any standard English word you input.");
		System.out.println("Type ! at any time to exit.");
		System.out.println();
		
		while(numRhymes <= 0) {
		
			System.out.print("How many rhymes would you like per word? ");
			numRhymes = scanner.nextInt();
			scanner.nextLine();
			
			/* User must specify a number greater than 0. They will be prompted repeatedly until
			 * they input a number greater than 0. */
			if (numRhymes <= 0) {
				System.out.println();
				System.out.println("Please choose a number greater than 0.");
				System.out.println();
			}
			
		}
		
		System.out.println("Thanks. You will receive " + numRhymes + " rhymes per word.");
		System.out.println();
		
		// Prompts user for first input word.
		System.out.print("Enter a word: ");
		word = scanner.nextLine();
		
		do {
			
			/* If there are any spaces in the word, the Datamuse API will not return
			 * rhymes, thus the input word cannot have spaces in it. */
			while (word.contains(" ")) {
				System.out.println("There cannot be any spaces in your word.");
				System.out.print("Please enter a new word: ");
			
				word = scanner.nextLine();
				
				System.out.println();
				System.out.println();
			}
			
			/* To easily request data from the Datamuse API, we can modify a standard
			 * URL, adjusting the end of it to match the input word as needed. urlString
			 * will be the beginning of the URL, to which we will concatenate the user
			 * input and retrieve rhymes from. */
			urlString = "https://api.datamuse.com/words?rel_rhy=" + word;
			System.out.println("Finding rhymes for " + word + "...");
			
			try {
				
				// Creating a URL object from the complete URL created with the user input.
				URL url = new URL (urlString);
				
				// Opening a connection with this URL.
				connection = (HttpURLConnection) url.openConnection();
				
				responseContent = new StringBuffer("");
				
				// Setting up the data request.
				connection.setRequestMethod("GET");
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				
				// status will tell us if the connection request was successful
				int status = connection.getResponseCode();
				
				//Need to read the input stream from HttpURLConnection
				
				if (status > 299) { //Connection was unsuccessful
					// We will read from the connection error stream to determine what went wrong.
					reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
						while ((line = reader.readLine() )!= null) {
							responseContent.append(line);
						}
						reader.close();
				} else { // Connection was successful
					// We will read from the connection input stream to get the response content.
					reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					while ((line = reader.readLine() )!= null) {
						responseContent.append(line);
					}
					reader.close();
				}
				
				// Now that we have the response content, we need to parse it.
				parse(responseContent.toString(), numRhymes);
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				connection.disconnect();
			}
			
			System.out.print("Enter a word: ");
			word = scanner.nextLine();
			
		} while (!word.equals("!"));
		
		System.out.println("Thanks for visiting!");
		
		scanner.close();
	}
	
	public static String parse(String responseBody, int numRhymes) {
		
		/* We will convert our response content to a JSON array with the 
		 * Maven JSON repository.*/
		JSONArray rhymes = new JSONArray(responseBody);
		
		int wordsCounter = 0;
		
		// If rhymes.length() == 0  then the input word was not English.
		if (rhymes.length() == 0) {
			System.out.println("This program can only find rhymes for real English words.");
		}
		
		// We iterate through the JSON array and print each word (rhyme).
		for (int i = 0; i < rhymes.length() && i < numRhymes; i++) {
			JSONObject rhymeObject = rhymes.getJSONObject(i);
			String word = rhymeObject.getString("word");
			
			if (word.length() <= 1) {
				/* To avoid printing a word of length 1, numRhymes is incremented
				 * and we don't print the word, effectively skipping it. */
				numRhymes++;
				
			} else if (word.contains(" ")){
				numRhymes++;
			} else {
				System.out.println(word);
				wordsCounter++;
			}
			
			if (wordsCounter == 0) {
				System.out.println("No rhymes to display. This program only prints perfect phonetic rhymes - no slant rhymes.");
			}

		}
		
		System.out.println();
		return null;
	}

}
