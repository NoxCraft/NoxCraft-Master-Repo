package net.noxcraft.core;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.easymock.EasyMock.*;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.PlayerManager;
import com.noxpvp.core.data.NoxPlayer;

@RunWith(Parameterized.class)
public class PlayerManagerTest extends EasyMockSupport {
	static PlayerManager playerManager;
	static File temp;
	static Server server;

	private static boolean multiFile;
	private static String player;
	
	public PlayerManagerTest(boolean multi, String player)
	{
		multiFile = multi;
		PlayerManagerTest.player = player;
	}
	
	@BeforeClass
	public static void setupClass() throws Exception {
		server = EasyMock.createMock(Server.class);
		expect(server.getLogger()).andStubReturn(Logger.getLogger("minecraft"));
		expect(server.getName()).andStubReturn("FakeServer");
		expect(server.getVersion()).andStubReturn("1.6.4");
		expect(server.getBukkitVersion()).andStubReturn("FakeBukkit 1.6.4-R9000");

		replay(server);
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
		expect(server.getLogger()).andReturn(Logger.getLogger("minecraft"));
		replay(server);
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
		expect(server.getLogger()).andReturn(Logger.getLogger("minecraft"));
		
		replay(server);
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
