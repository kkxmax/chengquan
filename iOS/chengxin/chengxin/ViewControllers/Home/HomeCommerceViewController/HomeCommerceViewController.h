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
}
@property (nonatomic, strong) IBOutlet UICollectionView *commerceCollectionView;
@property (nonatomic, weak) IBOutlet UIActivityIndicatorView *indicatorView;
- (void)getProductFromServer:cityName PleixingIds:(NSString*)pleixingIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword;
@property (nonatomic) NSInteger currentSortOrderIndex;
@property (nonatomic, strong) IBOutlet UIButton *addButton;
@end
