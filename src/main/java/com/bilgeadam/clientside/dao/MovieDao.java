package com.bilgeadam.clientside.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.IntStream;

import com.bilgeadam.bautils.BAUtils;
import com.bilgeadam.clientside.dbconnection.DatabaseInformation;
import com.bilgeadam.clientside.dto.MovieDto;
import com.bilgeadam.clientside.mdbfactory.MovieDbFactory;
import com.bilgeadam.clientside.menuitems.MenuItems;
import com.bilgeadam.clientside.serverconnection.MovieClient;
import com.bilgeadam.globalization.Globalization;
import com.bilgeadam.resources.Resources;

public class MovieDao implements ICommonDao<MovieDto>, Serializable {
	private static DatabaseInformation databaseInformation = new DatabaseInformation();
	
	@Override
	public void read(MovieDto movieDto) {
		Scanner scanner = new Scanner(System.in);
		BAUtils baUtils = BAUtils.getInstance();
		ResourceBundle lang = Globalization.getLanguageBundle();
		int choice;
		do {
			baUtils.menuBodyCreator(MenuItems.getInstance().getDbQueryList(Globalization.getLanguageBundle()),
					Globalization.getLanguageBundle().getString("Globalization.CHOICEQUERY"));
			choice = scanner.nextInt();
			if (choice < 0 || choice > 5)
				System.out.println(lang.getString("Globalization.INVALIDENTRY"));
		} while (choice < 0 || choice > 5);
		
		switch (choice) {
			case 1 -> yearBasedQuery(lang);
			case 2 -> genreBasedQuery(lang);
			case 3 -> movieBasedQuery(lang);
			case 4 -> actorBasedQuery(lang);
			case 5 -> MenuItems.getInstance().showMainMenu(lang);
			default -> throw new IllegalStateException("Unexpected value: " + choice);
		}
	}
	
	private void actorBasedQuery(ResourceBundle lang) {
		MovieClient movieClient = new MovieClient();
		movieClient.start(lang);
		MenuItems.getInstance().showMainMenu(lang);
	}
	
	@Override
	public void write(MovieDto movieDto) {
		MenuItems menuItems = MenuItems.getInstance();
		ResourceBundle langBundle = Globalization.getLanguageBundle();
		String result = "";
		try (Connection connection = getInterfaceConnection()) {
			String sql = """
					select exists(
					 SELECT datname FROM pg_catalog.pg_database WHERE lower(datname) = lower('MyMDB')
					);""";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				result = resultSet.getString("exists");
				
			}
			if (result.equals("f")) {
				sql = "CREATE DATABASE MyMDB";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.executeUpdate();
				connection.close();
				Thread.sleep(1000);
				createTable();
				System.out.println(langBundle.getString("Globalization.DBCREATED"));
			} else {
				System.out.println(langBundle.getString("Globalization.DBEXISTS"));
			}
		} catch (SQLException | InterruptedException e) {
			System.out.println("MovieDao Write() Method Failed: " + e.getMessage());
			e.printStackTrace();
		} finally {
			menuItems.showMainMenu(Globalization.getLanguageBundle());
		}
		
	}
	
	@Override
	public void delete(MovieDto movieDto) {
		MenuItems menuItems = MenuItems.getInstance();
		ResourceBundle langBundle = Globalization.getLanguageBundle();
		String result = "";
		
		try (Connection connection = DriverManager.getConnection(Resources.getConfig("dbUrl"),
				Resources.getConfig("dbUserName"), Resources.getConfig("dbPassword"))) {
			String sql = """
					select exists(
					 SELECT datname FROM pg_catalog.pg_database WHERE lower(datname) = lower('MyMDB')
					);""";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				result = resultSet.getString("exists");
				System.out.println(result);
			}
			if (result.equals("t")) {
				sql = "DROP DATABASE MyMDB";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.executeUpdate();
				System.out.println(langBundle.getString("Globalization.DBDELETED"));
			} else {
				System.out.println(langBundle.getString("Globalization.DBNOTEXIST"));
			}
		} catch (SQLException e) {
			System.out.println("MovieDao Write() Method Failed: " + e.getMessage());
			e.printStackTrace();
		} finally {
			menuItems.showMainMenu(Globalization.getLanguageBundle());
		}
	}
	
	private static void createTable() {
		databaseInformation.setUrl(Resources.getConfig("myDbUrl"));
		databaseInformation.setUserName(Resources.getConfig("mymdbUserName"));
		try (Connection connection2 = DriverManager.getConnection(databaseInformation.getUrl(),
				databaseInformation.getUserName(), Resources.getConfig("dbPassword"))) {
			String sql = "BEGIN; CREATE TABLE IF NOT EXISTS public.movies ( \"movieId\" serial NOT NULL, title character varying, genres character varying, PRIMARY KEY (\"movieId\") );\n" +
					"CREATE TABLE IF NOT EXISTS public.links (\"movieId\" serial NOT NULL, \"imdbId\" integer, \"tmdbId\" integer);\n" +
					"CREATE TABLE IF NOT EXISTS public.ratings (\"userId\" integer NOT NULL,\"movieId\" integer,rating real,\"timestamp\" integer);\n" +
					"CREATE TABLE IF NOT EXISTS public.tags (\"userId\" integer,\"movieId\" integer,tag character varying,\"timestamp\" integer);\n" +
					"ALTER TABLE IF EXISTS public.links ADD FOREIGN KEY (\"movieId\") REFERENCES public.movies (\"movieId\") MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION NOT VALID;\n" +
					"ALTER TABLE IF EXISTS public.ratings ADD FOREIGN KEY (\"movieId\") REFERENCES public.movies (\"movieId\") MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION NOT VALID;\n" +
					"ALTER TABLE IF EXISTS public.tags ADD FOREIGN KEY (\"movieId\") REFERENCES public.movies (\"movieId\") MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION NOT VALID;\n" +
					"END";
			PreparedStatement preparedStatement = connection2.prepareStatement(sql);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			insertIntoTable(connection2);
		} catch (SQLException e) {
			System.out.println("MovieDao createTable() Method Failed: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void insertIntoTable(Connection connection) {
		String movies = """
				COPY movies
				FROM 'C:\\bilgeadam\\csv\\movies.csv'\s
				DELIMITER ','\s
				CSV HEADER;""";
		String links = """
				COPY links
				FROM 'C:\\bilgeadam\\csv\\links.csv'\s
				DELIMITER ','\s
				CSV HEADER;""";
		
		String ratings = """
				COPY ratings
				FROM 'C:\\bilgeadam\\csv\\ratings.csv'\s
				DELIMITER ','\s
				CSV HEADER;""";
		String tags = """
				COPY tags
				FROM 'C:\\bilgeadam\\csv\\tags.csv'\s
				DELIMITER ','\s
				CSV HEADER;""";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(movies);
			preparedStatement.executeUpdate();
			preparedStatement = connection.prepareStatement(links);
			preparedStatement.executeUpdate();
			preparedStatement = connection.prepareStatement(ratings);
			preparedStatement.executeUpdate();
			preparedStatement = connection.prepareStatement(tags);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Movide Dao insertIntoTable() Method Failed: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void movieBasedQuery(ResourceBundle lang) {
		BAUtils baUtils = BAUtils.getInstance();
		String choice = (baUtils.readString(lang.getString("Globalization.MOVIEQUERY")));
		/*DatabaseInformation databaseInformation = new DatabaseInformation();
		databaseInformation.setUrl(Resources.getConfig("myDbUrl"));
		databaseInformation.setUserName(Resources.getConfig("dbUserName"));
		databaseInformation.setPassword(Resources.getConfig("dbPassword"));*/
		try (Connection connection = DriverManager.getConnection(Resources.getConfig("myDbUrl"),
				Resources.getConfig("dbUserName"), Resources.getConfig("dbPassword"))) {
			getFirstResult(choice, lang, connection);
		} catch (SQLException e) {
			System.out.println("MovieDao movieBasedQuery() Method Failed: " + e.getMessage());
			e.printStackTrace();
		}
		MenuItems.getInstance().showMainMenu(lang);
	}
	
	private static String yearController(String temp) {
		int index = temp.length();
		return temp.substring(index - 5, index - 1);
	}
	
	private static String titleController(String temp) {
		String title = "";
		for (char c : temp.toCharArray()) {
			if (c == '(')
				title = temp.substring(0, temp.indexOf('('));
		}
		return title;
	}
	
	private void genreBasedQuery(ResourceBundle lang) {
		BAUtils baUtils = BAUtils.getInstance();
		boolean isValid = true;
		String str = "";
		List<String> genresList = MenuItems.getInstance().getGenresList(lang);
		List<String> genresEn = MenuItems.getInstance().genresForQuery(lang);
		List<String> temp = new ArrayList<>();
		baUtils.menuBodyCreator(genresList, lang.getString("Globalization.GENRESLIST"));
		String choice = baUtils.readString(lang.getString("Globalization.GENREQUERY"));
		for (int i = 0; i < genresList.size(); i++) {
			if (choice.equalsIgnoreCase(genresList.get(i)))
				str = genresEn.get(i);
		}
		if (str.equals("")) {
			System.out.println(lang.getString("Globalization.INVALIDENTRY"));
			MenuItems.getInstance().showMainMenu(lang);
		}
		while (isValid) {
			try (Connection connection = DriverManager.getConnection(Resources.getConfig("myDbUrl"),
					Resources.getConfig("dbUserName"), Resources.getConfig("dbPassword"))) {
				String sql = "select m.title from movies as m where m.genres like '%" + str
						+ "%' order by m.\"movieId\" desc limit 10";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				// preparedStatement.setString(1, choice); //I'm having trouble with this
				// situation
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					temp.add(resultSet.getString("title"));
				}
				if (temp.size() == 0) {
					System.out.println(lang.getString("Globalization.NOQUERYRESULTS"));
					MenuItems.getInstance().showMainMenu(lang);
				} else
					baUtils.menuBodyCreator(temp, lang.getString("Globalization.RESULT"));
			} catch (SQLException e) {
				System.out.println("MovieDao genreBasedQuery() method failed." + e.getMessage());
				e.printStackTrace();
				isValid = false;
			}
			isValid = false;
		}
		MenuItems.getInstance().showMainMenu(lang);
	}
	
	private void yearBasedQuery(ResourceBundle lang) {
		BAUtils baUtils = BAUtils.getInstance();
		MenuItems menuItems = MenuItems.getInstance();
		List<String> animationMovies = new ArrayList<>();
		List<String> dramaMovies = new ArrayList<>();
		List<String> comedyMovies = new ArrayList<>();
		List<String> actionMovies = new ArrayList<>();
		List<String> crimeMovies = new ArrayList<>();
		List<String> tempList = new ArrayList<>();
		int choice = baUtils.readInt(lang.getString("Globalization.YEARQUERY"));
		if (choice < 1900 || choice > Calendar.getInstance().get(Calendar.YEAR)) {
			System.out.println(lang.getString("Globalization.INVALIDENTRY"));
			MenuItems.getInstance().showMainMenu(lang);
		}
		
		try (Connection connection = DriverManager.getConnection(Resources.getConfig("myDbUrl"),
				Resources.getConfig("dbUserName"), Resources.getConfig("dbPassword"))) {
			
			String sql = "select m.title from movies as m where m.genres like '%Animation%' and m.title like '%"
					+ choice + "%' order by m.\"movieId\" asc limit 10";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			// preparedStatement.setString(1, choice); //I'm having trouble with this
			// situation
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				tempList.add(resultSet.getString("title"));
			}
			for (String s : tempList)
				IntStream.range(0, s.length()).filter(i -> s.charAt(i) == '(').mapToObj(i -> s.substring(0, i))
						.forEach(animationMovies::add);
			tempList.clear();
			preparedStatement.close();
			resultSet.close();
			
			// SQL Query For Drama Movies
			sql = "select m.title from movies as m where m.genres like '%Drama%' and m.title like '%" + choice
					+ "%' order by m.\"movieId\" asc limit 10";
			preparedStatement = connection.prepareStatement(sql);
			// preparedStatement.setString(1, choice); //I'm having trouble with this
			// situation
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				tempList.add(resultSet.getString("title"));
			}
			for (String s : tempList)
				IntStream.range(0, s.length()).filter(j -> s.charAt(j) == '(').mapToObj(j -> s.substring(0, j))
						.forEach(dramaMovies::add);
			tempList.clear();
			preparedStatement.close();
			resultSet.close();
			
			// SQL Query For Comedy Movies
			sql = "select m.title from movies as m where m.genres like '%Comedy%' and m.title like '%" + choice
					+ "%' order by m.\"movieId\" asc limit 10";
			preparedStatement = connection.prepareStatement(sql);
			// preparedStatement.setString(1, choice);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				tempList.add(resultSet.getString("title"));
			}
			for (String s : tempList)
				IntStream.range(0, s.length()).filter(j -> s.charAt(j) == '(').mapToObj(j -> s.substring(0, j))
						.forEach(comedyMovies::add);
			tempList.clear();
			preparedStatement.close();
			resultSet.close();
			
			// SQL Query For Action Movies
			sql = "select m.title from movies as m where m.genres like '%Action%' and m.title like '%" + choice
					+ "%' order by m.\"movieId\" asc limit 10";
			preparedStatement = connection.prepareStatement(sql);
			// preparedStatement.setString(1, choice);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				tempList.add(resultSet.getString("title"));
			}
			for (String s : tempList)
				IntStream.range(0, s.length()).filter(j -> s.charAt(j) == '(').mapToObj(j -> s.substring(0, j))
						.forEach(actionMovies::add);
			tempList.clear();
			preparedStatement.close();
			resultSet.close();
			
			// SQL Query For Crime Movies
			sql = "select m.title from movies as m where m.genres like '%Crime%' and m.title like '%" + choice
					+ "%' order by m.\"movieId\" asc limit 10";
			preparedStatement = connection.prepareStatement(sql);
			// preparedStatement.setString(1, choice);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				tempList.add(resultSet.getString("title"));
			}
			for (String s : tempList)
				IntStream.range(0, s.length()).filter(j -> s.charAt(j) == '(').mapToObj(j -> s.substring(0, j))
						.forEach(crimeMovies::add);
			tempList.clear();
			preparedStatement.close();
			resultSet.close();
			
			// Printing Query Results
			baUtils.menuBodyCreator(animationMovies,
					lang.getString("Globalization.RESULT") + lang.getString("Globalization.ANIMATION"));
			baUtils.menuBodyCreator(dramaMovies,
					lang.getString("Globalization.RESULT") + lang.getString("Globalization.DRAMA"));
			baUtils.menuBodyCreator(comedyMovies,
					lang.getString("Globalization.RESULT") + lang.getString("Globalization.COMEDY"));
			baUtils.menuBodyCreator(actionMovies,
					lang.getString("Globalization.RESULT") + lang.getString("Globalization.ACTION"));
			baUtils.menuBodyCreator(crimeMovies,
					lang.getString("Globalization.RESULT") + lang.getString("Globalization.CRIME"));
			
		} catch (SQLException e) {
			System.out.println("MovieDao yearBasedQuery() method Failed." + e.getMessage());
			e.printStackTrace();
		} finally {
			menuItems.showMainMenu(Globalization.getLanguageBundle());
		}
	}
	
	private static MovieDto getFirstResult(String query, ResourceBundle lang, Connection connection) {
		MovieDbFactory movieDbFactory = new MovieDbFactory();
		MovieDto movieDto = new MovieDto();
		ICommonDao commonDao = movieDbFactory.getDao(2);
		String title = "", genre = "";
		double maxRating = 0.0, minRating = 0.0;
		int movieId = 0;
		long maxDate = 0, minDate = 0;
		
		try {
			String sql = "select m.\"movieId\",m.title,m.genres,r.rating,r.\"timestamp\" from movies as m inner join ratings as r on r.\"movieId\"= m.\"movieId\" where LOWER(m.title) LIKE '%" +
					query.toLowerCase() + "%' and r.rating= (select MAX(r.rating) from ratings as r where r.\"movieId\"= m.\"movieId\" ) order by m.\"movieId\" asc limit 1";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			//preparedStatement.setString(1, query.toLowerCase());
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				movieId = resultSet.getInt("movieId");
				title = resultSet.getString("title");
				genre = resultSet.getString("genres");
				maxRating = resultSet.getDouble("rating");
				maxDate = resultSet.getLong("timestamp");
			}
			preparedStatement.clearParameters();
			sql = "select r.rating as minirate,r.\"timestamp\" as minitime from ratings as r where r.\"movieId\" = ? order by minirate asc limit 1";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, movieId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				minRating = resultSet.getDouble("minirate");
				minDate = resultSet.getLong("minitime");
			}
			
			movieDto.setTitle(titleController(title));
			movieDto.setYear(yearController(title));
			movieDto.setGenre(genre);
			movieDto.setMaxRating(maxRating);
			movieDto.setMinRating(minRating);
			movieDto.setMaxTimeStamp(new Date(maxDate*1000L));
			movieDto.setMinTimeStamp(new Date(minDate*1000L));
			
			System.out.println(movieDto);
			commonDao.read(movieDto);
		} catch (SQLException e) {
			System.out.println("MovieDao getFirstResult() method Failed." + e.getMessage());
			e.printStackTrace();
		}
		return movieDto;
	}

}
