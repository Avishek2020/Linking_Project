SELECT count(?instance)
WHERE { 
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/l_album_label> }
	UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/l_album_track> }
	UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/l_album_url> }
	UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/l_artist_artist> }
	UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/l_artist_label> }
    UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/l_artist_track> }
	UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/l_artist_url> }
	UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/l_label_label> }
    UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/l_label_track> }
	UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/l_label_url> }
    UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/l_track_track> }
	UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/l_track_url> }
    UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/link_attribute> }
	UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/link_attribute_type> } 
    UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/lt_album_album> }
	UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/lt_album_artist> }
    UNION  
      { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/lt_album_label> }
    UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/lt_album_track> }
	UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/lt_album_url> }
    UNION  
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/lt_artist_artist> }
	UNION  
      { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/lt_artist_label> }
    UNION  
	 { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/lt_artist_track> }
	UNION  
     { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/lt_artist_url> }
    UNION  
     { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/lt_label_label> }
    UNION  
	 { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/lt_label_track> }
	UNION  
     { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/lt_label_url> }
	UNION
	 { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/lt_track_track> }
	UNION
     { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/lt_track_url> }
    UNION
	 { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/lt_url_url> }
	UNION
	 { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/puid> }
	UNION
	 { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/puidjoin> }
	UNION
	 { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/release_tag> }
	UNION
	 { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/script> }
	UNION
	 { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/script_language> }
	UNION 
	 { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/trackwords> }
	UNION
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/url> }
	UNION
	  { ?instance a <http://dbtune.org/musicbrainz/resource/vocab/wordlist> }
		  

  }