package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.Playlist;
import com.example.demo.entities.Songs;
import com.example.demo.services.PlayListService;
import com.example.demo.services.SongService;

@Controller
public class PlayListController {
	
	@Autowired
	PlayListService pserv;
	
	@Autowired
	SongService sserv;
	
	@GetMapping("/map-createplaylist")
	public String createAdminPlaylist(Model model) {
		//fetch the songs using SongService
		List<Songs> songslist = sserv.fetchAllSongs();
		// adding the songs in the model
		model.addAttribute("songslist", songslist);
		// sending Create PlayList
		return "createplaylist";
	}
	
	@PostMapping("/addplaylist")
	public String addPlaylist(@ModelAttribute Playlist playlist) {
		
		//adding PlayList
	    pserv.addPlaylist(playlist);
	    
	    //update PlayList
	    List<Songs> songsList = playlist.getSongs();
	    for(Songs song : songsList) {
	    	song.getPlaylist().add(playlist);
	    	sserv.updateSong(song);
	    }
	    return "playlistsuccess";
		
	}
	
	@GetMapping("/map-viewplaylists")
	public String viewPlaylist(Model model) {
		List<Playlist> plist = pserv.fetchPlaylists();
		model.addAttribute("plist", plist);
		return "viewplaylists";
	}
}


