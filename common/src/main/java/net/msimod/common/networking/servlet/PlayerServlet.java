package net.msimod.common.networking.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import net.msimod.common.MsiMod;

/**
 * The servlet that opens routes for getting player info via the api
 */
public class PlayerServlet extends HttpServlet {
    /**
     * The player info
     */
    public static class PlayerInfo {
        public String name;
        public int ping;
        public int x;
        public int y;
        public int z;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var playerInfo = GetPlayerInfo();
        var json = new Gson().toJson(playerInfo);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    /**
     * Get the player info
     * 
     * @return The player info
     */
    public HashMap<String, PlayerInfo> GetPlayerInfo() {
        var server = MsiMod.MINECRAFT_SERVER;
        var players = server.getPlayerList();

        var playerInfo = new HashMap<String, PlayerInfo>();
        for (var name : players.getPlayerNamesArray()) {
            var player = players.getPlayerByName(name);
            if (player == null)
                continue;

            var info = new PlayerInfo();
            info.name = name;
            info.ping = player.latency;
            info.x = (int) player.getX();
            info.y = (int) player.getY();
            info.z = (int) player.getZ();
            playerInfo.put(player.getUUID().toString(), info);
        }

        return playerInfo;
    }
}
