package vollt_tuning;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import adql.db.DBColumn;

import tap.ServiceConnection;
import tap.TAPException;
import tap.TAPExecutionReport;
import tap.formatter.VOTableFormat;
import tap.metadata.TAPColumn;
import tap.metadata.TAPCoosys;
import mapper.ProductMapper;
import uk.ac.starlink.votable.DataFormat;
import uk.ac.starlink.votable.VOSerializer;
import uk.ac.starlink.votable.VOTableVersion;
import utils.FileGetter;
import utils.WalkerGetter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;

public class CustomVOTableFormat extends VOTableFormat {

	/* **********************************************************************
	   * NOTE:                                                              *
	   *   Attribut de classe de VOTableFormat intéressant pour accéder à   *
	   *   la base de données ou métadonnées:                               *
	   *                                                                    *
	   *                     ServiceConnection service;                     *
	   *                                                                    *
	   ********************************************************************** */

	/**
	 * NOTE:
	 *   Il doit y avoir au moins un constructeur avec un seul paramètre de type
	 *   ServiceConnection. Il est possible d'avoir d'autres constructeurs, mais
	 *   en passant seulement par le fichier de configuration, seul celui
	 *   ci-dessous sera appelé.
	 */
	public CustomVOTableFormat(final ServiceConnection service) throws NullPointerException {
		//super(service);                                             // Serialisation et version par défaut: BINARY et 1.3
		super(service, DataFormat.TABLEDATA);                     // Pour préciser une sérialisation différente
		//super(service, null, VOTableVersion.V12);                 // Serialisation par défaut (BINARY) et version précise
		//super(service, DataFormat.TABLEDATA, VOTableVersion.V12); // Serialisation et version personnalisées
		
		// Si vous voulez aussi changer le type MIME associé avec ce format:
		//setMimeType(mimeType, shortForm);
	}
	
	@Override
	protected void writeHeader(final VOTableVersion votVersion, final TAPExecutionReport execReport, final BufferedWriter out) throws IOException, TAPException {
		
		/* ******************************************************************
		   *                                                                *
		   * NOTE:                                                          *
		   *   Tout ce qui suit est un copier-coller de la fonction         *
		   *   VOTableFormat.writeHeader(...). A changer selon les besoins  *
		   *   mais attention à respecter le schéma VOTable et les headers  *
		   *   TAP (surtout la description des colonnes). Le plus simple    *
		   *   étant d'ajouter les headers nécessaires à la fin (cf NOTE    *
		   *   plus bas).                                                   *
		   *                                                                *
		   ****************************************************************** */
		
		//Get the query to annotate or not
		
		String query = execReport.parameters.getQuery();
		
		// Set the root VOTABLE node:
		out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		out.newLine();
		out.write("<VOTABLE" + VOSerializer.formatAttribute("version", votVersion.getVersionNumber()) + VOSerializer.formatAttribute("xmlns", votVersion.getXmlNamespace()) + VOSerializer.formatAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance") + VOSerializer.formatAttribute("xsi:schemaLocation", votVersion.getXmlNamespace() + " " + votVersion.getSchemaLocation()) + ">");
		out.newLine();
		
		if (query.startsWith("SELECT * FROM column_grouping.column_grouping_table")) {
			
			out.write("<VODML>\n");
			
			String fileName = "vizier_grouped_col.mango.config.json";
			FileGetter getter = new FileGetter(fileName);

		    try {
		      FileGetter templateGetter = new FileGetter("mango.mapping.xml");
		      File mangoTemplate = templateGetter.GetFile();
		      System.out.println("Template loaded correctly");
		      WalkerGetter templateWalker = new WalkerGetter(mangoTemplate);
		      TreeWalker walker = templateWalker.getWalker();
			  File jsonFile = getter.GetFile();
			  ProductMapper mapper = new ProductMapper(jsonFile);
			  walker.getRoot();
			  mapper.BuildAnnotations(out,walker);

		    } catch (Exception e) {
		      e.printStackTrace();
		    }	
			finally {
				out.write("</VODML>");
				out.newLine();
			}

		}
	
		out.newLine();
		
		// The RESOURCE note MUST have a type "results":	[REQUIRED]
		out.write("<RESOURCE type=\"results\">");
		out.newLine();

		// Indicate that the query has been successfully processed:	[REQUIRED]
		out.write("<INFO name=\"QUERY_STATUS\" value=\"OK\"/>");
		out.newLine();

		// Append the PROVIDER information (if any):	[OPTIONAL]
		if (service.getProviderName() != null) {
			out.write("<INFO name=\"PROVIDER\"" + VOSerializer.formatAttribute("value", service.getProviderName()) + ">" + ((service.getProviderDescription() == null) ? "" : VOSerializer.formatText(service.getProviderDescription())) + "</INFO>");
			out.newLine();
		}

		// Append the ADQL query at the origin of this result:	[OPTIONAL]
		String adqlQuery = execReport.parameters.getQuery();
		if (adqlQuery != null) {
			out.write("<INFO name=\"QUERY\"" + VOSerializer.formatAttribute("value", adqlQuery) + "/>");
			out.newLine();
		}

		// Append the fixed ADQL query, if any:	[OPTIONAL]
		String fixedQuery = execReport.fixedQuery;
		if (fixedQuery != null) {
			out.write("<INFO name=\"QUERY_AFTER_AUTO_FIX\"" + VOSerializer.formatAttribute("value", fixedQuery) + "/>");
			out.newLine();
		}

		// Insert the definition of all used coordinate systems:
		HashSet<String> insertedCoosys = new HashSet<String>(10);
		for(DBColumn col : execReport.resultingColumns) {
			// ignore columns with no coossys:
			if (col instanceof TAPColumn && ((TAPColumn)col).getCoosys() != null) {
				// get its coosys:
				TAPCoosys coosys = ((TAPColumn)col).getCoosys();
				// insert the coosys definition ONLY if not already done because of another column:
				if (!insertedCoosys.contains(coosys.getId())) {
					// write the VOTable serialization of this coordinate system definition:
					out.write("<COOSYS" + VOSerializer.formatAttribute("ID", coosys.getId()));
					if (coosys.getSystem() != null)
						out.write(VOSerializer.formatAttribute("system", coosys.getSystem()));
					if (coosys.getEquinox() != null)
						out.write(VOSerializer.formatAttribute("equinox", coosys.getEquinox()));
					if (coosys.getEpoch() != null)
						out.write(VOSerializer.formatAttribute("epoch", coosys.getEpoch()));
					out.write(" />");
					out.newLine();
					// remember this coosys has already been written:
					insertedCoosys.add(coosys.getId());
				}
			}
		}
		
		
		/* ******************************************************************
		   *                                                                *
		   * NOTE:                                                          *
		   *   Ajouter les nouveaux header ici!                             *
		   *                                                                *
		   ****************************************************************** */

		out.flush();
	}
}


