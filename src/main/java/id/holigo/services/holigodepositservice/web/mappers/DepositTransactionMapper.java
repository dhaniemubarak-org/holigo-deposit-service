package id.holigo.services.holigodepositservice.web.mappers;


import id.holigo.services.common.model.TransactionDto;
import id.holigo.services.common.model.UserDtoForUser;
import id.holigo.services.holigodepositservice.domain.DepositTransaction;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@DecoratedWith(DepositTransactionMapperDecorator.class)
@Mapper
public interface DepositTransactionMapper {


    @Mapping(target = "voucherCode", ignore = true)
    @Mapping(target = "transactionType", ignore = true)
    @Mapping(target = "serviceId", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "pointAmount", ignore = true)
    @Mapping(target = "paymentServiceId", ignore = true)
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "invoiceNumber", ignore = true)
    @Mapping(target = "indexUser", ignore = true)
    @Mapping(target = "indexProduct", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "id", ignore = true)
    TransactionDto depositTransactionToTransactionDto(DepositTransaction depositTransaction, UserDtoForUser userDtoForUser);
}
