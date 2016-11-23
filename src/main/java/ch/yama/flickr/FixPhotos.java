package ch.yama.flickr;

import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FixPhotos implements CommandLineRunner {

	Logger logger = LoggerFactory.getLogger(FixPhotos.class);

	@Autowired
	FlickrService flickrService;

	public static void main(String... args) {
		SpringApplication.run(FixPhotos.class, args);
	}

	@Override
	public void run(String... args) throws IOException, InterruptedException {

		final Collection<Photo> photos = flickrService.searchPhotos().forUser("142925583@N05").withExtras("date_upload,date_taken").get();
		photos.forEach(photo -> {
			final String title = photo.getTitle();
			final String dateTaken = photo.getDateTaken();
			final String id = photo.getId();
			if (dateTaken != null && !"".equals(dateTaken.trim())) {
				// On a une date de prise de vue.
				if (!dateTaken.equals(title)) {
					logger.info("Updating photo #" + id + ", title => " + dateTaken);
					flickrService.setPhotoMeta().forPhoto(id).withTitle(dateTaken).post();
				}
			} else {
				logger.warn(String.format("Photo #%s does not have a 'date taken'", id));
			}
		});
	}
}
