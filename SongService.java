package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Songs;

public interface SongService {
	
	public String addSongs(Songs songs);
	
	public boolean songExists(String name);

	public List<Songs> fetchAllSongs();

	public void updateSong(Songs song);

}
