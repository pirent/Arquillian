package com.github.pirent;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class GamePersistentTest {

	@Deployment
	public static Archive<?> createDeployement() {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war");
		archive.addPackage(Game.class.getPackage());
		archive.addAsResource("test-persistence.xml", "META-INF/persistence.xml");
		archive.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		
		return archive;
	}
	
	private static final String[] GAME_TITLES = {
		"Ninja Gaiden",
		"Battlefield",
		"Saints Row IV"
	};
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	UserTransaction utx;
	
	// ----------------------------------------------------------- ||
	// --------------- Persistence-related Setup ----------------- ||
	// ----------------------------------------------------------- ||
	
	@Before
	public void preparePersistenceTest() throws Exception {
		clearData();
		insertData();
		startTransaction();
	}

	@After
	public void commitTransaction() throws Exception {
		utx.commit();
	}
	
	// ----------------------------------------------------------- ||
	// -----------------Tests ------------------------------------ ||
	// ----------------------------------------------------------- ||
	
	@Test
	public void shouldFindAllGamesUsingJpqlQuery() throws Exception {
		String fetchingAllGameInJpql = "select g from Game g order by g.id";
		
		System.out.println("Selecting (using JPQL)...");
		List<Game> games = em.createQuery(fetchingAllGameInJpql, Game.class).getResultList();
		
		System.out.println("Found " + games.size() + " games (using JPQL):");
		assertContainsAllGames(games);
	}
	
	// ----------------------------------------------------------- ||
	// ---------------- Internals Helper Methods ----------------- ||
	// ----------------------------------------------------------- ||
	
	private static void assertContainsAllGames(Collection<Game> retrievedGames) {
		Assert.assertEquals(GAME_TITLES.length, retrievedGames.size());
		final Set<String> retrievedGameTitles = new HashSet<String>();
		
		for (Game game : retrievedGames) {
			System.out.println("* " + game);
			retrievedGameTitles.add(game.getTitle());
		}
		Assert.assertTrue(retrievedGameTitles.containsAll(Arrays.asList(GAME_TITLES)));
	}
	
	private void clearData() throws Exception {
		utx.begin();
		em.joinTransaction();
		System.out.println("Dumping old records...");
		em.createQuery("delete from Game").executeUpdate();
		utx.commit();
	}
	
	private void insertData() throws Exception {
		utx.begin();
		em.joinTransaction();
		System.out.println("Inserting records...");
		for (String title : GAME_TITLES) {
			Game game = new Game(title);
			em.persist(game);
		}
		utx.commit();
		// Clear the persistence context (first-level cache)
		em.clear();
	}
	
	private void startTransaction() throws Exception {
		utx.begin();
		em.joinTransaction();
	}
}
