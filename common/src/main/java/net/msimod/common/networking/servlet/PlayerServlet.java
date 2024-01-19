package net.msimod.common.networking.servlet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import net.msimod.common.MsiMod;
import net.msimod.common.auth.Auth;
import net.msimod.common.networking.dto.PlayerDto;

/**
 * The servlet that opens routes for getting player info via the api
 */
public class PlayerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!Auth.validateToken(request, response)) {
            return;
        }

        var PlayerDto = GetPlayerDto();
        var json = new Gson().toJson(PlayerDto);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    /**
     * Get the player info
     * 
     * @return The player info
     */
    public List<PlayerDto> GetPlayerDto() {
        var server = MsiMod.MINECRAFT_SERVER;
        var players = server.getPlayerList();

        var result = new LinkedList<PlayerDto>();
        for (var name : players.getPlayerNamesArray()) {
            var player = players.getPlayerByName(name);
            if (player == null)
                continue;

            var info = new PlayerDto();
            info.uuid = player.getUUID().toString();
            info.name = name;
            info.ping = player.latency;
            info.x = (int) player.getX();
            info.y = (int) player.getY();
            info.z = (int) player.getZ();
            result.add(info);
        }

        return result;
    }
}
