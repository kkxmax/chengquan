//
//  HomeCommerceViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
@interface HomeCommerceViewController : BaseViewController<UICollectionViewDelegate, UICollectionViewDataSource>
{
}
@property (nonatomic, strong) IBOutlet UICollectionView *commerceCollectionView;
- (void)getProductFromServer:cityName PleixingIds:(NSString*)pleixingIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword;
@property (nonatomic) NSInteger currentSortOrderIndex;
- (void)refreshTopItems;
- (void)refreshBottomItems;
- (void)addAction;
@end
