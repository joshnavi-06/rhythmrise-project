package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.Songs;
import com.example.demo.services.SongService;

@Controller
public class SongsController {
	
	@Autowired
	SongService sserv;
	
	@PostMapping("/addsongs")
	public String addSongs(@ModelAttribute Songs songs) {
		boolean songstatus = sserv.songExists(songs.getName());
		if(songstatus == false)
		{
			sserv.addSongs(songs);
			System.out.println("Song added");
			return "songsuccess";
		}
		else
		{
			return "songaddedfail";
		}
	}
	
	@GetMapping("/map-viewsongs")
	public String viewSongs(Model model) {
		List<Songs> ls = sserv.fetchAllSongs();
		model.addAttribute("songs", ls);
		return "displaysongs";
	}
	
	@GetMapping("/viewsongs")
	public String viewCustomerSongs(Model model) {
		boolean primeCustomerstatus = true;
		if(primeCustomerstatus == true) 
		{
			List<Songs> ls = sserv.fetchAllSongs();
			model.addAttribute("songs", ls);
			return "displaysongs";
		}
		else
		{
		    return "makepayment";
	    }
	}
			
}
