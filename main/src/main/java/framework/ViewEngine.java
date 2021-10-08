package framework;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

public class ViewEngine {

    private final PebbleEngine engine;

    public ViewEngine(ServletContext servletContext) {
        ServletLoader loader = new ServletLoader(servletContext);
        loader.setCharset("UTF-8");
        //System.out.println("路径template"+ new File("src/main/webapp/WEB-INF/templates").getAbsolutePath());
        loader.setPrefix("WEB-INF/templates");
        loader.setSuffix("");
        this.engine = new PebbleEngine.Builder().autoEscaping(true).cacheActive(false) // no cache for dev
                .loader(loader).build();
    }

    public void render(ModelAndView mv, Writer writer) throws IOException {
        PebbleTemplate template = this.engine.getTemplate(mv.view);
        template.evaluate(writer, mv.model);
    }
}
