package org.tanrabad.survey.presenter.maps;

interface MapFragmentInterface {
    void setMapCanScrolled(boolean isCanScroll);

    void setMoveToMyLocation(boolean isMoved);

    void setMapZoomable(boolean isZoomable);

    void setShowMyLocation(boolean isShowed);

    void setMoveToMyLocationButtonEnabled(boolean isMyLocationButtonEnabled);
}
