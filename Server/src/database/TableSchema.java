/**
 * Package contenente le classi per la gestione dell'accesso al database.
 */
package Server.src.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe che rappresenta lo schema di una tabella del database.
 * Consente di recuperare i metadati delle colonne di una tabella specifica, tra cui nomi e tipi.
 */
public class TableSchema {
	/** Riferimento all'oggetto di accesso al database */
	private DbAccess db;

	/**
	 * Classe interna che rappresenta una colonna della tabella.
	 * Memorizza il nome e il tipo di una colonna, con metodi per accedere a queste informazioni.
	 */
	public class Column {
		/** Nome della colonna */
		private String name;

		/** Tipo della colonna mappato in formato Java */
		private String type;

		/**
		 * Costruisce un oggetto Column con il nome e tipo specificati.
		 *
		 * @param name nome della colonna
		 * @param type tipo della colonna
		 */
		Column(String name, String type) {
			this.name=name;
			this.type=type;
		}

		/**
		 * Restituisce il nome della colonna.
		 *
		 * @return nome della colonna
		 */
		public String getColumnName(){
			return name;
		}

		/**
		 * Verifica se la colonna è di tipo numerico.
		 *
		 * @return true se la colonna è di tipo numerico, false altrimenti
		 */
		public boolean isNumber() {
			return type.equals("number");
		}

		/**
		 * Restituisce una rappresentazione testuale della colonna nel formato "nome:tipo".
		 *
		 * @return stringa rappresentante la colonna
		 */
		public String toString(){
			return name + ":" + type;
		}
	}

	/** Lista delle colonne che compongono lo schema della tabella */
	List<Column> tableSchema = new ArrayList<Column>();

	/**
	 * Costruisce un oggetto TableSchema per la tabella specificata.
	 * Recupera i metadati delle colonne e li memorizza nella lista tableSchema.
	 *
	 * @param db oggetto di accesso al database
	 * @param tableName nome della tabella di cui recuperare lo schema
	 * @throws SQLException se si verificano errori SQL durante l'accesso ai metadati
	 * @throws DatabaseConnectionException se si verificano errori di connessione al database
	 */
	public TableSchema(DbAccess db, String tableName) throws SQLException, DatabaseConnectionException {
		this.db = db;
		HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();
		// http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");

		Connection con = db.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet res = meta.getColumns(null, null, tableName, null);

		while (res.next()) {
			if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME"))) {
				tableSchema.add(new Column(res.getString("COLUMN_NAME"), mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));
			}
		}
		res.close();
	}

	/**
	 * Restituisce il numero di attributi (colonne) nello schema della tabella.
	 *
	 * @return numero di attributi
	 */
	public int getNumberOfAttributes() {
		return tableSchema.size();
	}

	/**
	 * Restituisce la colonna in posizione specificata.
	 *
	 * @param index indice della colonna da recuperare
	 * @return oggetto Column corrispondente all'indice specificato
	 */
	public Column getColumn(int index) {
		return tableSchema.get(index);
	}
}