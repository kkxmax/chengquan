//
//  CategorySearchViewController.h
//  chengxin
//
//  Created by seniorcoder on 11/2/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CategorySearchViewController : UIViewController
{
}
@property (nonatomic, strong) IBOutlet UISearchBar *categorySearchbar;
@property (nonatomic, strong) IBOutletCollection(UIButton) NSArray *categoryButtons;
@end
