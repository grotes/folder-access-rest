package jpa.tablename;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;
/**************
 * 
 * Override naming of tables in jpa in order to extract the name from properties if it starts with &
 * 
 * @author grotes
 *
 **************/
public class RealNamingStrategyImpl extends org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String dollar = "${";
	private static final String end = "}";
	
	public static final PhysicalNamingStrategyStandardImpl INSTANCE = new PhysicalNamingStrategyStandardImpl();

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
    	String text = name.getText();
    	if(name.getText().startsWith(dollar)){
    		Properties prop = new Properties();
    		InputStream input = null;
    		try {
    			input = PhysicalNamingStrategyStandardImpl.class.getClassLoader().getResourceAsStream("application.properties");
    			// load a properties file
    			prop.load(input);
    			// get the property value and print it out
    			text = prop.getProperty(name.getText().replace(dollar, "").replace(end, ""));
    		}catch(Exception e){e.printStackTrace();}
    	}
        return new Identifier(text, name.isQuoted());
    }

   
}