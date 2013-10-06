package eu.speedbadminton.pyramid.controller.ajax;

import eu.speedbadminton.pyramid.model.Player;
import eu.speedbadminton.pyramid.service.PlayerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * User: Yoann Moranville
 * Date: 03/10/2013
 *
 * @author Yoann Moranville
 */
@Controller
public class UserDataAjaxController extends AjaxAbstractController {
    private static final Logger LOG = Logger.getLogger(UserDataAjaxController.class);

    @Autowired
    private PlayerService playerService;

    @RequestMapping(value={"/usersDataColorbox"}, method = RequestMethod.POST)
    public void getUserData(HttpServletRequest request, HttpServletResponse response) {
        try {
            String userId = request.getParameter("id");
            Writer writer = getResponseWriter(response);
            Player player = playerService.getPlayerById(userId);
            writeUserData(writer, player.getName(), player.getEmail());
            closeWriter(writer);
        } catch (IOException e) {
            LOG.error("Error...", e);
        }
    }

    @RequestMapping(value = {"/usersEncounterQuestion"}, method = RequestMethod.POST)
    public void sendEncounter(HttpServletRequest request, HttpServletResponse response) {
        String askerId = request.getParameter("asker");
        Player player = playerService.getPlayerById(askerId);
        String askedEmail = request.getParameter("asked");
        try {
            Writer writer = getResponseWriter(response);
            if(playerService.sendEmail(player, askedEmail)) {
                writeSimpleData(writer, "success", "true");
            } else {
                writeSimpleData(writer, "success", "false");
            }
            closeWriter(writer);
        } catch (IOException e) {
            LOG.error("Error...", e);
        }
    }
}