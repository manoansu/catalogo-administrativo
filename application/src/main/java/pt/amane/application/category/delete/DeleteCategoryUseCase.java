package pt.amane.application.category.delete;

import pt.amane.UnitUseCase;

/**
 * In java world, you always work for abstration, not for implmentation,
 * because when this class has a new feature , you can only change the implementation not abstration.
 */
public sealed abstract class DeleteCategoryUseCase
    extends UnitUseCase<String>
    permits DefaultDeleteCategoryUseCase {

}
