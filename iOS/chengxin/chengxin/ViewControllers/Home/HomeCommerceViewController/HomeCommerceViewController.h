//
//  HomeCommerceViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeCommerceViewController : UIViewController<UICollectionViewDelegate, UICollectionViewDataSource>
{
    NSTimer *slideCommercePageTimer;
}
@property (nonatomic, strong) IBOutlet UIScrollView *slideCommerceScrollView;
@property (nonatomic, strong) IBOutlet UIPageControl *slideCommercePageCtrl;
@property (nonatomic, strong) IBOutlet UICollectionView *commerceCollectionView;
@end
