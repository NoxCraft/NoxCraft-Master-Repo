package net.noxcraft.core;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;

import static org.mockito.Mockito.*;


import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.PlayerManager;
import com.noxpvp.core.data.NoxPlayer;

@RunWith(Parameterized.class)
public class PlayerManagerTest{
	static PlayerManager playerManager;
	static File temp;
	@Mock static Server server;

	private static boolean multiFile;
	private static String player;
	
	private static void setMulti(boolean a)
	{
		multiFile = a;
	}
	
	private static void setPlayer(String pl)
	{
		player = pl;
	}
	
	public PlayerManagerTest(boolean multi, String player)
	{
		setMulti(multi);
		setPlayer(player);
	}
	
	@BeforeClass
	public static void setupClass() throws Exception {
		server = mock(Server.class);
		
		when(server.getLogger()).thenReturn(Logger.getLogger("minecraft"));
		when(server.getName()).thenReturn("FakeServer");
		when(server.getVersion()).thenReturn("1.6.4");
		
		when(server.getBukkitVersion()).thenReturn("FakeBukkit 1.6.4-R9000");

		Bukkit.setServer(server);
		
		playerManager = new PlayerManager(new FileConfiguration((temp = new File("playerdata"+File.separator+"config.yml")))){
			@Override
			public ConfigurationNode getPlayerNode(String name) {
				File f = new File("playerdata"+File.separatorChar+player+".yml");
				f.deleteOnExit();
				if (isMultiFile())
					return new FileConfiguration(f);
				else if (config != null)
					return config.getNode("players").getNode(name);
				else
					return null;
			}
		};
		
		temp.deleteOnExit();
		
		NoxCore.setUseUserFile(multiFile);
	}
	
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { {true, "Coaster3000"}, {false, "Coaster3000"} , {true, "apexer"}, {false, "apexer"}, {true, "some/random"}, {false , "some/random"}, {true, "bbcsto13"}, {false, "bbcsto13"} };
		return Arrays.asList(data);
	}
	
	@AfterClass
	public static void teardownClass() {
		temp = null;
		
		playerManager = null;
		
		reset(server);
		
	}
	
	@Test
	public final void testPlayerManagerFileConfiguration() {
		reset(server);
		
		when(server.getLogger()).thenReturn(Logger.getLogger("minecraft"));

		assertNotNull("Player Manager failed to initialize", playerManager);
		assertNotNull("File was null. It did not aquire an object.",temp);
	}

	@Test
	@Ignore
	public final void testStore() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetPlayerNode() {
		reset(server);
		
		when(server.getLogger()).thenReturn(Logger.getLogger("minecraft"));

		ConfigurationNode node = playerManager.getPlayerNode("test");
		if (playerManager.isMultiFile())
			assertTrue("ConfigurationNode was not a FileConfiguration when it was explicitly stated to be so.", (node instanceof FileConfiguration));
		else
			assertTrue("ConfigurationNode was not supposed to be a file configuration.", (!(node instanceof FileConfiguration)));
	}

	@Test
	public final void testGetPlayerString() {
		NoxPlayer noxPlayer = playerManager.getPlayer(player);
		assertTrue(noxPlayer != null);
		assertTrue(noxPlayer.getName().equals(player));
	}

	@Test
	public final void testSavePlayerString() {
		playerManager.savePlayer(player);
		
//		assertTrue("File never saved", playerManager.getPlayerFile(player).exists());
	}

	@Test
	public final void testSavePlayerNoxPlayer() {
		NoxPlayer np = playerManager.getPlayer(player);
		assertNotNull("NoxPlayer was null when it should be found!", np);
		
		playerManager.savePlayer(playerManager.getPlayer(player));
		
//		assertTrue("File never saved", playerManager.getPlayerFile(player).exists());
	}
}
