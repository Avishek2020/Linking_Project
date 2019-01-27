List of Task Contains
------------------------
1) Dataset Used for Comparision MusicBrainz ,Magnatune and Jamendo
     http://dbtune.org/musicbrainz/sparql
	 http://dbtune.org/magnatune/sparql
	 http://dbtune.org/musicbrainz/
	 
2) Dataset Used for Comparision DrugBank and Siders
     http://drugbank.bio2rdf.org/sparql
	 http://sider.bio2rdf.org/sparql
	
3) Use of Sparql queries for getting resources and properties

 a) To get all classes 
    --------------------
    SELECT DISTINCT ?class
    WHERE { [] a ?class }
    ORDER BY ?class

 b) To get Properties 
    -------------------
	SELECT DISTINCT ?property
    WHERE { [] ?property [] }
    ORDER BY ?property
 c) To get Labels
    -------------------
	select ?label ?instance where { \n" + 
	              ?instance <http://www.w3.org/2000/01/rdf-schema#label> ?label .
	              ?instance a <http://purl.org/ontology/mo/MusicArtist> . } 
	             ORDER BY ?label
 d) Parameterised Sparql Query Using Apache Jena.
 
 e) To get properties and label of all classes.
    -------------------
	SELECT ?p ?o where 
{
	?s ?p ?o {select ?s where {?s a ?class {SELECT DISTINCT ?class WHERE { [] a ?class } ORDER BY ?class} } Limit 100 } 
	FILTER(isliteral(?o))

	filter(langMatches(lang(?o),"EN"))
	} 
 
4) Linking of two dataset Using Trigram appraoch.

5) Linking of two dataset Using Limes , passing one properties.

6) Linking of two dataset Using Limes , passing all properties.

7) Linking of two dataset Using Wombat Unsupervised Learning.

8) Linking of two dataset Using Jaccard Similarity.