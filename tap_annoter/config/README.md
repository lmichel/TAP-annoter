This folder is used to store any config we need.

mapping_components is the folder that hosts the mapping component files. These files are not all required so far, but all of them are already here.
Some json files are in here but the application does not use them. Only XML files are parsed and used.

usecases-conf is the folder where we must put the configuration of the mapping for our tables. These files have to be json files, and they must have
the same name as the table they are reffering to (schema included). You must add one of these file if you want to annotate a new table.

If somehow we need more config files, you can add another folder here but you will have to add it to the classpath in Eclipse
(Right click on the project -> Properties -> Java Build Path -> Source -> Add Folder and select you new folder)
