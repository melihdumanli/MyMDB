package com.bilgeadam.clientside.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.bilgeadam.clientside.dto.ActorDto;
import com.bilgeadam.resources.Resources;

public class ActorDao implements ICommonDao<ActorDto>, Serializable {
	
	@Override
	public void read(ActorDto actorDto) {
		String str = "", temp = "";
		List<String> titles = new ArrayList<>();
		List<String> tempList = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(Resources.getConfig("names")))) {
			while ((str = br.readLine()) != null) {
				if (str.contains(actorDto.getName())) {
					StringTokenizer st = new StringTokenizer(str, "\t");
					while (st.hasMoreTokens()) {
						st.nextToken();
						actorDto.setName(st.nextToken());
						actorDto.setBirthYear(st.nextToken());
						actorDto.setDeathYear(st.nextToken());
						actorDto.setPrimaryProfession(st.nextToken());
						temp = st.nextToken();
					}
				}
				if (temp != "")
					break;
			}
			
			StringTokenizer strTokenizer = new StringTokenizer(temp, ",");
			while (strTokenizer.hasMoreTokens()) {
				titles.add(strTokenizer.nextToken());
			}
			for (int i = 0; i < titles.size(); i++) {
				try (BufferedReader breader = new BufferedReader(new FileReader(Resources.getConfig("movies")))) {
					String line;
					while ((line = breader.readLine()) != null) {
						StringBuilder sb = new StringBuilder();
						if (line.contains(titles.get(i))) {
							StringTokenizer st = new StringTokenizer(line, "\t");
							while (st.hasMoreTokens()) {
								st.nextToken();
								st.nextToken();
								sb.append(st.nextToken() + " ");
								st.nextToken();
								st.nextToken();
								sb.append(st.nextToken() + " ");
								st.nextToken();
								st.nextToken();
								sb.append(st.nextToken() + " ");
							}
							tempList.add(sb.toString());
							sb.setLength(0);
						}
						if (tempList.size() == titles.size())
							break;
					}
				}
			}
			actorDto.setKnownForTitles(tempList);
		} catch (IOException e) {
			System.out.println("ActorDao read() Method Failed" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public void write(ActorDto actorDto) {
		try (BufferedWriter bufferedWriter = new BufferedWriter(
				new FileWriter(Resources.getConfig("filePath"), true))) {
			bufferedWriter.newLine();
			bufferedWriter.write(actorDto.toString());
			bufferedWriter.flush();
		} catch (IOException e) {
			System.out.println("ActorDao Write Error." + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public void delete(ActorDto actorDto) {
		// this method will not be needed for actors.
	}
	// test
}
