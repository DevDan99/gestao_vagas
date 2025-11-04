package br.com.danielcosta.gestao_vagas.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.danielcosta.gestao_vagas.providers.JWTProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // Indica que essa classe é um componente gerenciado pelo Spring, permitindo a injeção de dependências e o gerenciamento
				// do ciclo de vida do bean.
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private JWTProvider jwtProvider; // Injeção de dependência do JWTProvider, que é responsável por validar tokens JWT.

	@Override // Implementa o método doFilterInternal da classe OncePerRequestFilter, que é chamado para cada requisição HTTP.
	// O método doFilterInternal é onde você pode adicionar a lógica de filtragem personalizada para cada requisição.
	// Ele recebe o objeto HttpServletRequest (representando a requisição), HttpServletResponse (representando a resposta) e
	// FilterChain (para continuar a cadeia de filtros).
	// Dentro desse método, você pode implementar a lógica de autenticação, autorização ou qualquer outra verificação de
	// segurança necessária.
	// Após a lógica personalizada, você deve chamar filterChain.doFilter(request, response) para continuar a cadeia de
	// filtros e permitir que a requisição prossiga.
	// Se a requeisição não for autorizada, você pode lançar uma exceção ou configurar a resposta HTTP adequadamente.
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

		String header = request.getHeader("Authorization"); // Pega o token do cabeçalho Authorization

		if (request.getRequestURI().startsWith("/company")) {
			/*
			 * Verifica se o cabeçalho Authorization está presente e começa com "Bearer "
			 * Se o cabeçalho não estiver presente ou não começar com "Bearer ", a requisição continua sem autenticação.
			 * Se o cabeçalho estiver presente e começar com "Bearer ", o token é extraído e validado.
			 * Se o token for válido, o contexto de segurança é atualizado com as informações do usuário autenticado.
			 * Se o token for inválido, a resposta HTTP é configurada para 401 Unauthorized e a cadeia de filtros é interrompida.
			 * Isso garante que apenas requisições com tokens válidos possam acessar recursos protegidos.
			 */
			if (header != null) {
				var token = this.jwtProvider.validateToken(header); // Valida o token (isso deve ser implementado no JWTProvider)

				/*
				 * Se o token for inválido, retorna 401 Unauthorized e interrompe a cadeia de filtros Se o token for válido, continua a
				 * cadeia de filtros Aqui você pode adicionar lógica adicional, como definir o contexto de segurança com as informações
				 * do usuário autenticado.
				 * Por exemplo, você pode usar o Spring Security para definir o contexto de segurança.
				 * SecurityContextHolder.getContext().setAuthentication(authentication); Onde "authentication" é um objeto que
				 * representa o usuário autenticado.
				 * Isso permitirá que o Spring Security reconheça o usuário autenticado em requisições subsequentes.
				 * Se o token for inválido, você pode lançar uma exceção ou configurar a resposta HTTP adequadamente.
				 * Aqui, estamos apenas retornando 401 Unauthorized para simplificar. Em um cenário real, você pode querer fornecer mais
				 * detalhes sobre o erro.
				 */

				if (token == null) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Se o token for inválido, retorna 401 Unauthorized
					return; // Interrompe a cadeia de filtros
				}

				var roles = token.getClaim("roles").asList(Object.class); // Pega as roles do token
				var grants = roles.stream() // Mapeia as roles para GrantedAuthority
						.map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase())) // Adiciona o prefixo "ROLE_" conforme a convenção do Spring Security
						.toList(); // Coleta as GrantedAuthority em uma lista

				/*
				 * A Classe SimpleGrantedAuthority representa uma autoridade concedida a um usuário.
				 * No contexto do Spring Security, uma autoridade é uma permissão ou privilégio que um usuário possui.
				 * As autoridades são usadas para controlar o acesso a recursos protegidos com base nas permissões do usuário.
				 * A classe SimpleGrantedAuthority é uma implementação simples da interface GrantedAuthority, que é usada pelo
				 * Spring Security para representar uma autoridade concedida a um usuário.
				 * Cada instância de SimpleGrantedAuthority contém uma única autoridade, que é representada como uma string.
				 * No exemplo acima, estamos mapeando as roles extraídas do token JWT para instâncias de SimpleGrantedAuthority,
				 * adicionando o prefixo "ROLE_" conforme a convenção do Spring Security.
				 * Isso permite que o Spring Security reconheça as roles corretamente ao realizar verificações de autorização.
				 */

				request.setAttribute("companyId", token.getSubject()); // Adiciona o ID da empresa como um atributo na requisição

				// Exemplo de como definir o contexto de segurança (isso deve ser adaptado conforme sua implementação)
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(token.getSubject(), null, grants);

				SecurityContextHolder.getContext().setAuthentication(auth); // Define o contexto de segurança com o usuário autenticado
			}
		}

		filterChain.doFilter(request, response); // Continua a cadeia de filtros
	}

}
