package pt.amane.infrastructure.castmember.presenter;

import pt.amane.application.castmember.retrieve.get.GetCastMemberByIdOutput;
import pt.amane.application.castmember.retrieve.list.ListCastMembersOutput;
import pt.amane.infrastructure.castmember.model.CastMemberListResponse;
import pt.amane.infrastructure.castmember.model.CastMemberResponse;

public interface CastMemberPresenter {

  static CastMemberResponse present(final GetCastMemberByIdOutput aMember) {
    return new CastMemberResponse(
        aMember.id(),
        aMember.name(),
        aMember.type().name(),
        aMember.createdAt().toString(),
        aMember.updatedAt().toString()
    );
  }

  static CastMemberListResponse present(final ListCastMembersOutput aMember) {
    return new CastMemberListResponse(
        aMember.id(),
        aMember.name(),
        aMember.type().name(),
        aMember.createdAt().toString()
    );
  }
}
