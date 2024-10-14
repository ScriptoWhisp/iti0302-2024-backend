package ee.taltech.iti03022024project.service;

import ee.taltech.iti03022024project.dto.OrderDto;
import ee.taltech.iti03022024project.mapstruct.OrderMapper;
import ee.taltech.iti03022024project.domain.OrderEntity;
import ee.taltech.iti03022024project.repository.OrderRepository;
import ee.taltech.iti03022024project.domain.StatusEntity;
import ee.taltech.iti03022024project.repository.StatusRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StatusRepository statusRepository;
    private final OrderMapper orderMapper;


    public List<OrderDto> getOrders() {
        return orderRepository.findAll().stream().map(orderMapper::toDto).toList();
    }

    public Optional<OrderDto> getOrderById(int id) {
        return orderRepository.findById(id).map(orderMapper::toDto);
    }

    public Optional<OrderDto> createOrder(OrderDto orderDto) {
        OrderEntity newOrder = orderMapper.toEntity(orderDto);
        OrderEntity savedOrder = orderRepository.save(newOrder);
        return Optional.of(orderMapper.toDto(savedOrder));
    }

    public Optional<OrderDto> updateOrder(int id, OrderDto orderDto) {
        Optional<OrderEntity> orderToUpdate = orderRepository.findById(id);
        orderToUpdate.ifPresent(order -> {
            Optional<StatusEntity> newStatusOpt = statusRepository.findById(orderDto.getStatusId());
            newStatusOpt.ifPresent(order::setStatus);
            orderRepository.save(order);
        });
        return orderToUpdate.map(orderMapper::toDto);
    }

    public Optional<Object> deleteOrder(int id) {
        Optional<OrderEntity> orderToDelete = orderRepository.findById(id);
        orderToDelete.ifPresent(orderRepository::delete);
        return orderToDelete.map(orderMapper::toDto);
    }
}
