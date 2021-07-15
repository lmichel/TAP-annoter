This folder is the appender folder.

You will find every appender in here, and if you want to add an appender, you should put it here.

The appenders are used to add a specific type of measure in the VOTable. They also manage to add the right globals.

You will find more details in the code.

IdentifierAppender.java is a bit special though because it is modifying the template directly and not "adding" anything but its behaviour is quite simple.

ProductMapper.java is the class that is allowed to call the right appender when needed by parsing every measure in the json config file. It could be 
assimilated as the conductor of the appenders.
