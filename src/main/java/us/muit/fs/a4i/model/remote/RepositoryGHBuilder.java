/**
 * 
 */
package us.muit.fs.a4i.model.remote;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHRepositoryStatistics;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GHRepositoryStatistics.CodeFrequency;

import us.muit.fs.a4i.model.entities.Metric;
import us.muit.fs.a4i.model.entities.Metric.MetricBuilder;
import us.muit.fs.a4i.model.entities.Report;
import us.muit.fs.a4i.model.entities.ReportI;

/**
 * @author Isabel Rom�n
 *
 */
public class RepositoryGHBuilder extends RemoteGHBuilder {
	private static Logger log = Logger.getLogger(RepositoryGHBuilder.class.getName());
	/**
	 * <p>
	 * Informaci�n sobre el repositorio obtenida de GitHub
	 * </p>
	 */
	private GHRepository remoteRepo;
	/**
	 * <p>
	 * Entidad local, con la informaci�n de inter�s. Puede incluir informaci�n
	 * directa o calculada
	 * </p>
	 * <p>
	 * La versi�n actual incluye informaci�n obtenida de una �nica consulta
	 * <p>
	 */
	private ReportI myRepo;

	public RepositoryGHBuilder() {
		super();
	}

	@Override
	public ReportI buildReport(String repositoryId) {
		log.info("Invocado el m�todo que construye un objeto RepositoryReport");
		/**
		 * <p>
		 * En estos momentos cada vez que se invoca construyeObjeto se crea y rellena
		 * uno nuevo
		 * </p>
		 * <p>
		 * Deuda t�cnica: se puede optimizar consultando s�lo las diferencias respecto a
		 * la fecha de la �ltima representaci�n local
		 * </p>
		 */

		try {
			log.info("Nombre repo = " + repositoryId);

			GitHub gb = getConnection();
			remoteRepo = gb.getRepository(repositoryId);
			log.info("le�do " + remoteRepo);
			myRepo = new Report(repositoryId);

			/**
			 * M�tricas directas de tipo conteo
			 */

			MetricBuilder<Integer> subscribers = new Metric.MetricBuilder<Integer>("subscribers",
					remoteRepo.getSubscribersCount());
			subscribers.description("N�mero de suscriptores, watchers en la web").unit("subscribers").source("GitHub");
			myRepo.addMetric(subscribers.build());
			log.info("A�adida m�trica suscriptores " + subscribers);

			MetricBuilder<Integer> forks = new Metric.MetricBuilder<Integer>("forks", remoteRepo.getForksCount());
			forks.description("N�mero de forks, no son los foks de la web").source("GitHub");
			myRepo.addMetric(forks.build());
			log.info("A�adida m�trica forks " + forks);

			MetricBuilder<Integer> watchers = new Metric.MetricBuilder<Integer>("watchers",
					remoteRepo.getWatchersCount());
			watchers.description("Observadores, en la web aparece com forks").source("GitHub");
			myRepo.addMetric(watchers.build());

			MetricBuilder<Integer> stars = new Metric.MetricBuilder<Integer>("stars", remoteRepo.getStargazersCount());
			stars.description("Estrellas otorgadas").source("GitHub");
			myRepo.addMetric(stars.build());

			MetricBuilder<Integer> issues = new Metric.MetricBuilder<Integer>("issues", remoteRepo.getOpenIssueCount());
			issues.source("GitHub").description("N�mero de tareas sin cerrar");
			myRepo.addMetric(issues.build());
			/**
			 * M�tricas directas de tipo fecha
			 */

			MetricBuilder<Date> creation = new Metric.MetricBuilder<Date>("creation", remoteRepo.getCreatedAt());
			creation.source("GitHub").description("Fecha de creaci�n del repositorio");
			myRepo.addMetric(creation.build());

			MetricBuilder<Date> push = new Metric.MetricBuilder<Date>("lastPush", remoteRepo.getPushedAt());
			push.description("�ltimo push realizado en el repositorio").source("GitHub");
			myRepo.addMetric(push.build());

			MetricBuilder<Date> updated = new Metric.MetricBuilder<Date>("lastUpdated", remoteRepo.getUpdatedAt());
			push.description("�ltima actualizaci�n").source("GitHub");
			myRepo.addMetric(updated.build());
			/**
			 * M�tricas m�s elaboradas, requieren m�s "esfuerzo"
			 */

			GHRepositoryStatistics data = remoteRepo.getStatistics();
			List<CodeFrequency> codeFreq = data.getCodeFrequency();
			int additions = 0;
			int deletions = 0;
			for (CodeFrequency freq : codeFreq) {

				if ((freq.getAdditions() != 0) || (freq.getDeletions() != 0)) {
					Date fecha = new Date((long) freq.getWeekTimestamp() * 1000);
					log.info("Fecha modificaciones " + fecha);
					additions += freq.getAdditions();
					deletions += freq.getDeletions();
				}

			}
			MetricBuilder<Integer> totalAdditions = new Metric.MetricBuilder<Integer>("additions", additions);
			totalAdditions.source("GitHub, calculada")
					.description("Suma el total de adiciones desde que el repositorio se cre�");
			myRepo.addMetric(totalAdditions.build());

			MetricBuilder<Integer> totalDeletions = new Metric.MetricBuilder<Integer>("deletions", deletions);
			totalDeletions.source("GitHub, calculada")
					.description("Suma el total de borrados desde que el repositorio se cre�");
			myRepo.addMetric(totalDeletions.build());

		} catch (Exception e) {
			log.severe("Problemas en la conexi�n " + e);
		}

		return myRepo;
	}

}
