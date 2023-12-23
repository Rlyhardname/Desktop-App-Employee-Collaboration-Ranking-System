package com.sirma.academy.dao;

import java.util.List;
import java.util.Map;

public interface EntityDAO<T> {
    public List<T> mapOfObjects(Long id);
}
