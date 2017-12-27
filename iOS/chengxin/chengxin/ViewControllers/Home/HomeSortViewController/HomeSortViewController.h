//
//  HomeSortViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeSortViewController : UIViewController
{
}
@property (nonatomic, strong) IBOutletCollection(UILabel) NSArray *sortNameLabel;
@property (nonatomic, strong) IBOutletCollection(UIImageView) NSArray *sortCheckImage;

@end
