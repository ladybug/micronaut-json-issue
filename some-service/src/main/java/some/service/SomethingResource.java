package some.service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Random;

import javax.inject.Inject;
import javax.validation.Valid;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import org.hibernate.id.uuid.StandardRandomStrategy;

@Controller("/something")
public class SomethingResource {

	@Inject
	SomeRepository someRepository;

	public SomethingResource(SomeRepository someRepository) {
		this.someRepository = someRepository;
	}

	@Post
	public HttpResponse<Something> createSomething(@Valid @Body Something something) throws URISyntaxException {

		something.setId(new Random().nextLong());
		something.setName(randomlyGeneratedString());
		something.setValue(random(49));

		someRepository.save(something);
		return HttpResponse.<Something>created(new URI("/something/" + something.getId())).body(something);
	}

	private String randomlyGeneratedString() {
		byte[] array = new byte[7]; // length is bounded by 7
		new Random().nextBytes(array);
		return new String(array, Charset.forName("UTF-8"));
	}

	public static BigDecimal random(int range) {
		BigDecimal max = new BigDecimal(range);
		BigDecimal randFromDouble = new BigDecimal(Math.random());
		BigDecimal actualRandomDec = randFromDouble.multiply(max);
		actualRandomDec = actualRandomDec
				.setScale(2, BigDecimal.ROUND_DOWN);
		return actualRandomDec;
	}
}
