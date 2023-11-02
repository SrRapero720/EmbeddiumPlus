package vice.templatemod.registry.autoreg;

import lombok.SneakyThrows;
import lombok.val;
import vice.templatemod.TemplateMod;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AutoRegistry
{
    public static void initClasses(String path)
    {
        Instant start = Instant.now();
        val classes = getClasses(path);
        Instant finish = Instant.now();

        TemplateMod.LOGGER.info("AUTOREGISTRY: FETCHING CLASSES TOOK " + Duration.between(start, finish).toNanos() + " NANOS, " + Duration.between(start, finish).toMillis() + " MILLIS");
    }

    @SneakyThrows
    public static List<Class> getClasses(String packageName)
    {
        val loader = Thread.currentThread().getContextClassLoader();
        assert loader != null;

        String path = packageName.replace('.', '/');
        val resources = loader.getResources(path);

        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        List<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        assert files != null;

        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
