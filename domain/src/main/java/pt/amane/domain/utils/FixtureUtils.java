package pt.amane.domain.utils;


import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.List;
import static io.vavr.API.Match;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Random;
import java.util.Set;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastMemberType;
import pt.amane.domain.category.Category;
import pt.amane.domain.genre.Genre;
import pt.amane.domain.video.AudioVideoMedia;
import pt.amane.domain.video.ImageMedia;
import pt.amane.domain.video.Rating;
import pt.amane.domain.video.Resource;
import pt.amane.domain.video.Video;
import pt.amane.domain.video.VideoMediaType;


public final class FixtureUtils {

    public static String name() {
        return "Brad Pitt";
    }

    public static Integer year() {
        return new Random().nextInt(2020, 2030);
    }

    public static BigDecimal duration() {
        final BigDecimal aDuration[] = new BigDecimal[] {
            BigDecimal.valueOf(120.0),
            BigDecimal.valueOf(15.5),
            BigDecimal.valueOf(35.5),
            BigDecimal.valueOf(10.0),
            BigDecimal.valueOf(2.0)
        };
        return aDuration[new Random().nextInt(aDuration.length)];
    }

    public static boolean bool() {
        return new Random().nextBoolean();
    }

    public static String title() {
        return """
                System Design no Mercado Livre na prática,
                Não cometa esses erros ao trabalhar com Microsserviços,
                Testes de Mutação. Você não testa seu software corretamente
                """;
    }

    public static String checksum() {
        return "03fe62de";
    }

    public static Video video() {
        return Video.newVideo(
                FixtureUtils.title(),
                Videos.description(),
                Year.of(FixtureUtils.year()),
                FixtureUtils.duration(),
                FixtureUtils.bool(),
                FixtureUtils.bool(),
                Videos.rating(),
                Set.of(Categories.aulas().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.wesley().getId(), CastMembers.gabriel().getId())
        );
    }

    public static final class Categories {

        private static final Category AULAS =
                Category.newCategory("Aulas", "Some description", true);

        private static final Category LIVES =
                Category.newCategory("Lives", "Some description", true);

        public static Category aulas() {
            return Category.with(AULAS);
        }

        public static Category lives() {
            return Category.with(LIVES);
        }
    }

    public static final class CastMembers {

        private static final CastMember WESLEY =
                CastMember.newCastmember("Wesley FullCycle", CastMemberType.ACTOR);

        private static final CastMember GABRIEL =
                CastMember.newCastmember("Gabriel FullCycle", CastMemberType.ACTOR);

        public static CastMemberType type() {
            return getCastMemberEnumValue();
        }

        public static CastMember wesley() {
            return CastMember.with(WESLEY);
        }

        public static CastMember gabriel() {
            return CastMember.with(GABRIEL);
        }
    }

    public static final class Genres {

        private static final Genre TECH =
                Genre.newGenre("Technology", true);

        private static final Genre BUSINESS =
                Genre.newGenre("Business", true);

        public static Genre tech() {
            return Genre.with(TECH);
        }

        public static Genre business() {
            return Genre.with(BUSINESS);
        }
    }

    public static final class Videos {

        private static final Video SYSTEM_DESIGN = Video.newVideo(
                "System Design no Mercado Livre na prática",
                description(),
                Year.of(2022),
                FixtureUtils.duration(),
                FixtureUtils.bool(),
                FixtureUtils.bool(),
                rating(),
                Set.of(Categories.aulas().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.wesley().getId(), CastMembers.gabriel().getId())
        );

        public static Video systemDesign() {
            return Video.with(SYSTEM_DESIGN);
        }

        public static Rating rating() {
            return getRatingEnumValue();
        }

        public static VideoMediaType mediaType() {
            return getVideoMediaTypeEnumValue();
        }

        public static Resource resource(final VideoMediaType type) {
            final String contentType = Match(type).of(
                    Case($(List(VideoMediaType.VIDEO, VideoMediaType.TRAILER)::contains), "video/mp4"),
                    Case($(), "image/jpg")
            );

            final String checksum = IdUtils.uuid();
            final byte[] content = "Conteudo".getBytes();

            return Resource.with(content, checksum, contentType, type.name().toLowerCase());
        }

        public static String description() {
            final String aDescription[] = new String[]{
                    """
                            Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                            Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                            Para acessar todas as aulas, lives e desafios, acesse:
                            https://imersao.fullcycle.com.br/
                            """,
                    """
                            Nesse vídeo você entenderá o que é DTO (Data Transfer Object), quando e como utilizar no dia a dia, 
                            bem como sua importância para criar aplicações com alta qualidade.
                            """
            };

            return aDescription.toString();
        }

        public static AudioVideoMedia audioVideo(final VideoMediaType type) {
            final var checksum = FixtureUtils.checksum();
            return AudioVideoMedia.with(
                    checksum,
                    type.name().toLowerCase(),
                    "/videos/" + checksum
            );
        }

        public static ImageMedia image(final VideoMediaType type) {
            final var checksum = FixtureUtils.checksum();
            return ImageMedia.with(
                    checksum,
                    type.name().toLowerCase(),
                    "/images/" + checksum
            );
        }
    }

    /**
     *
     * @return the index positon of VideoMediaType enum value..
     */
    private static VideoMediaType getVideoMediaTypeEnumValue() {
        return VideoMediaType.values()[new Random().nextInt(VideoMediaType.values().length)];
    }

    /**
     *
     * @return the index positon of Rating enum value..
     */
    private static Rating getRatingEnumValue() {
        return Rating.values()[new Random().nextInt(Rating.values().length)];
    }

    /**
     *
     * @return the index positon of CastMember enum value..
     */
    private static CastMemberType getCastMemberEnumValue() {
        return CastMemberType.values()[new Random().nextInt(CastMemberType.values().length)];
    }
}
