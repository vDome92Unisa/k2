package control;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class ClickjackingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // Aggiungi l'header Content-Security-Policy per proteggere da Clickjacking
        httpServletResponse.setHeader("Content-Security-Policy", "frame-ancestors 'none'");

        // Aggiungi l'header X-Frame-Options per proteggere da Clickjacking (compatibilità con versioni precedenti dei browser)
        httpServletResponse.setHeader("X-Frame-Options", "DENY");

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nessuna inizializzazione necessaria
    }

    @Override
    public void destroy() {
        // Nessuna azione di pulizia necessaria
    }
}
